package com.umcspot.spot.network.multipart

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import javax.inject.Inject

class MultipartFactory @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /** core/model이 Uri를 몰라도 되도록 String을 받는 버전 */
    fun imagePartOrNull(uriString: String?, formKey: String = DEFAULT_FORM_KEY): MultipartBody.Part? {
        val raw = uriString?.trim().orEmpty()
        if (raw.isBlank()) return null

        val uri = parseToUriOrNull(raw) ?: return null
        return uriToPartOrNull(uri, formKey)
    }

    fun imagePartOrNull(uri: Uri?, formKey: String = DEFAULT_FORM_KEY): MultipartBody.Part? {
        if (uri == null) return null
        return uriToPartOrNull(uri, formKey)
    }

    private fun parseToUriOrNull(raw: String): Uri? {
        // 1) content://, file://, etc.
        runCatching { Uri.parse(raw) }.getOrNull()?.let { parsed ->
            // Uri.parse("content://...") 는 scheme이 존재
            if (!parsed.scheme.isNullOrBlank()) return parsed
        }

        // 2) scheme 없는 plain path일 수도 있음 -> file:// 로 보정
        return runCatching {
            val f = File(raw)
            if (f.exists()) Uri.fromFile(f) else null
        }.getOrNull()
    }

    private fun uriToPartOrNull(uri: Uri, formKey: String): MultipartBody.Part? {
        val cr = context.contentResolver

        // contentResolver가 열 수 없는 Uri면 null 처리(요청 자체를 막음)
        val canOpen = runCatching { cr.openInputStream(uri)?.close(); true }.getOrElse { false }
        if (!canOpen) return null

        val fileName = resolveFileName(cr = cr, uri = uri)
        val mime = cr.getType(uri) ?: "image/*"

        val body = object : RequestBody() {
            override fun contentType() = mime.toMediaTypeOrNull()

            override fun writeTo(sink: BufferedSink) {
                cr.openInputStream(uri)?.use { input ->
                    sink.writeAll(input.source())
                } ?: throw IllegalArgumentException("Cannot open InputStream for uri=$uri")
            }

            // (선택) 가능하면 길이 제공. content://는 모르는 경우가 많아서 -1 반환될 수 있음.
            override fun contentLength(): Long {
                return runCatching {
                    cr.openAssetFileDescriptor(uri, "r")?.use { it.length } ?: -1L
                }.getOrDefault(-1L)
            }
        }

        return MultipartBody.Part.createFormData(formKey, fileName, body)
    }

    private fun resolveFileName(cr: android.content.ContentResolver, uri: Uri): String {
        val nameFromQuery = runCatching {
            cr.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
                val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (c.moveToFirst() && idx >= 0) c.getString(idx) else null
            }
        }.getOrNull()

        val safe = nameFromQuery?.trim().takeIf { !it.isNullOrBlank() }
        if (safe != null) return safe

        // 마지막 path segment로 fallback (file:// 에서 유용)
        val fromPath = uri.lastPathSegment?.trim().takeIf { !it.isNullOrBlank() }
        if (fromPath != null) return fromPath

        return "image_${System.currentTimeMillis()}.jpg"
    }

    companion object {
        private const val DEFAULT_FORM_KEY = "imageFile"
    }
}
