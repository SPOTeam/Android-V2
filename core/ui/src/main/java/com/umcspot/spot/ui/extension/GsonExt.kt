package com.umcspot.spot.ui.extension

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson

fun Any.toRequestBody(): RequestBody {
    val jsonString = Gson().toJson(this)
    return jsonString.toRequestBody("application/json".toMediaTypeOrNull())
}

fun Any.toMultipartBodyPart(name: String): MultipartBody.Part {
    val requestBody = this.toRequestBody()
    return MultipartBody.Part.createFormData(name, null, requestBody)
}