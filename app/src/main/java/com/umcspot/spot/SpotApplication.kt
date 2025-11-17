package com.umcspot.spot

import android.app.Application
import android.util.Log
import com.umcspot.spot.buildconfig.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NidOAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpotApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        NidOAuth.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, BuildConfig.APP_NAME)
    }
}