package com.umcspot.spot.buildconfig.impl

import com.umcspot.spot.buildconfig.BuildConfig.BASE_URL
import com.umcspot.spot.buildconfig.BuildConfig.KAKAO_NATIVE_KEY
import com.umcspot.spot.common.BuildConfigFieldProvider
import com.umcspot.spot.common.BuildConfigFields


import javax.inject.Inject

class BuildConfigFieldsProviderImpl @Inject constructor() : BuildConfigFieldProvider {
    override fun get(): BuildConfigFields =
        BuildConfigFields(
            baseUrl = BASE_URL,
            kakaoNativeKey = KAKAO_NATIVE_KEY,
            isDebug = true
        )
}