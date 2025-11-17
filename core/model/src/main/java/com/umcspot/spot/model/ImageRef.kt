package com.umcspot.spot.model

sealed interface ImageRef {
    data object None : ImageRef
    data class LocalName(val name: String) : ImageRef            // drawable 이름
    data class LocalPath(val path: String) : ImageRef            // 로컬 파일 경로 (예: /storage/…)
    data class Url(val url: String) : ImageRef                   // 원격 이미지
}