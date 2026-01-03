package com.umcspot.spot.study.dto.request

import com.google.gson.annotations.SerializedName
import com.umcspot.spot.model.StudyStyle
import com.umcspot.spot.model.StudyTheme

data class StudyRequestDto(
    @SerializedName("name")
    val name: String,

    @SerializedName("maxMembers")
    val maxMembers: Int,

    @SerializedName("hasFee")
    val hasFee: Boolean,

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("categories")
    val categories: List<StudyTheme>,

    @SerializedName("styles")
    val styles: List<StudyStyle>,

    @SerializedName("regionCodes")
    val regionCodes: List<String>
)