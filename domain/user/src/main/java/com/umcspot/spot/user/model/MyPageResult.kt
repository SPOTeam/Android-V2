package com.umcspot.spot.user.model

import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.SocialLoginType

data class MyPageResult(
    val memberId: Long,
    val nickname: String,
    val profileImageUrl: ImageRef,
    val loginType: SocialLoginType,
    val email: String,
    val participateCount: Int,
    val recruitingCount: Int,
    val appliedCount: Int
)