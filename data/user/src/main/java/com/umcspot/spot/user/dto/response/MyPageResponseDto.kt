package com.umcspot.spot.user.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.WeatherType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MyPageResponseDto(
    @SerialName("memberId")
    val memberId : Long,
    @SerialName("nickname")
    val nickname : String,
    @SerialName("profileImageUrl")
    val profileImageUrl : String?,
    @SerialName("loginType")
    val loginType : String,
    @SerialName("email")
    val email : String,
    @SerialName("studyParticipationInfo")
    val studyParticipationInfo : StudyParticipationInfo,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StudyParticipationInfo(
    @SerialName("participatingStudyCount")
    val participatingStudyCount : Int,
    @SerialName("recruitingStudyCount")
    val recruitingStudyCount : Int,
    @SerialName("appliedStudyCount")
    val appliedStudyCount : Int
)
