package com.umcspot.spot.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

object FileUtil {
    fun createTempFileFromUri(context: Context, uri: Uri): File? {
        return runCatching {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap == null) return null

            val file = File.createTempFile("upload_image_", ".jpg", context.cacheDir)

            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            }

            bitmap.recycle()

            file
        }.onFailure { e ->
            Timber.e(e, "이미지 압축 및 임시 파일 생성 실패")
        }.getOrNull()
    }
}