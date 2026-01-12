package com.umcspot.spot.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project

internal fun Project.configureBuildConfig(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val properties = gradleLocalProperties(rootDir, providers)

    val baseUrl = properties.getProperty("BASE_URL") ?: ""
    val kakaoNativeKey = properties.getProperty("KAKAO_NATIVE_KEY") ?: ""
    val naverClientId = properties.getProperty("NAVER_CLIENT_ID") ?: ""
    val naverClientSecret = properties.getProperty("NAVER_CLIENT_SECRET") ?: ""
    val appName = properties.getProperty("APP_NAME") ?: "SPOT"

    val weatherUrl = properties.getProperty("WEATHER_BASE_URL") ?: ""
    val weatherToken = properties.getProperty("WEATHER_TOKEN") ?: ""

    commonExtension.apply {
        defaultConfig {

            buildConfigField(
                "String",
                "BASE_URL",
                "\"$baseUrl\""
            )

            buildConfigField(
                "String",
                "KAKAO_NATIVE_KEY",
                "\"$kakaoNativeKey\""
            )

            buildConfigField(
                "String",
                "NAVER_CLIENT_ID",
                "\"$naverClientId\""
            )

            buildConfigField(
                "String",
                "NAVER_CLIENT_SECRET",
                "\"$naverClientSecret\""
            )

            buildConfigField(
                "String",
                "APP_NAME",
                "\"$appName\""
            )

            buildConfigField(
                "String",
                "WEATHER_BASE_URL",
                "\"$weatherUrl\""
            )

            buildConfigField(
                "String",
                "WEATHER_TOKEN",
                "\"$weatherToken\""
            )
        }

        buildFeatures {
            buildConfig = true
        }
    }
}
