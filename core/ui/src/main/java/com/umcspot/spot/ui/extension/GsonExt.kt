package com.umcspot.spot.ui.extension

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import java.io.File

fun Any.toRequestBody(): RequestBody {
    val jsonString = Gson().toJson(this)
    return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
}

fun Any.toMultipartBodyPart(name: String): MultipartBody.Part {
    val requestBody = this.toRequestBody()
    return MultipartBody.Part.createFormData(name, null, requestBody)
}

fun Uri.toFile(context: Context): File? {
    val file = File(context.cacheDir, "memoir_${System.currentTimeMillis()}.jpg")
    return try {
        context.contentResolver.openInputStream(this)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        file
    } catch (e: Exception) {
        null
    }
}