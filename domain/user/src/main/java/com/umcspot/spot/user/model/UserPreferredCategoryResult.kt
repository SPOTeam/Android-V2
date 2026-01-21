package com.umcspot.spot.user.model

data class UserPreferredCategoryResult(
    val categories : List<String?>,
    val totalCount: Int
)