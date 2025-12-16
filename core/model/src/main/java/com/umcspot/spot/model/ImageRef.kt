package com.umcspot.spot.model

import com.sun.jndi.toolkit.url.Uri

sealed interface ImageRef {
    data object None : ImageRef
    data class Name(val name: String) : ImageRef
    data class Url(val url: String) : ImageRef
    data class LocalUri(val uri: String) : ImageRef

}

fun String?.toImageRef(): ImageRef {
    val s = this?.trim()
    return when {
        s.isNullOrEmpty() -> ImageRef.None
        s.equals("null", ignoreCase = true) -> ImageRef.None
        s.startsWith("http", ignoreCase = true) -> ImageRef.Url(s)
        else -> ImageRef.Name(s) // 리소스 네임일 수도 있음
    }
}