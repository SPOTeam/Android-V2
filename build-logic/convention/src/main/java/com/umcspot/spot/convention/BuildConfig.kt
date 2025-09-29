package com.umcspot.spot.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project

internal fun Project.configureBuildConfig(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        defaultConfig {
            val properties = gradleLocalProperties(rootDir, providers)

            buildConfigField(
                "String",
                "BASE_URL",
                "\"${properties.getProperty("base.url") ?: "https://default-url.com/"}\""
            )
            buildConfigField(
                "String",
                "KAKAO_NATIVE_KEY",
                "\"${properties.getProperty("kakao.native.key") ?: ""}\""
            )
        }

        buildFeatures {
            buildConfig = true
        }
    }
}