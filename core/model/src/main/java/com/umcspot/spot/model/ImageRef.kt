package com.umcspot.spot.model

sealed interface ImageRef {
    data object None : ImageRef
    data class Name(val name: String) : ImageRef            // drawable 이름
    data class Url(val url: String) : ImageRef                   // 원격 이미지
}