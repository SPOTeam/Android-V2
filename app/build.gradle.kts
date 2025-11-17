plugins {
    alias(libs.plugins.spot.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.spot.android.hilt)
}

android {
    namespace = "com.umcspot.spot"

    signingConfigs {
        getByName("debug") {
            storeFile = file("key/SpotKey")
            storePassword = "spotspot"
            keyAlias = "spotkey0"
            keyPassword = "spotspot"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.main)
    implementation(projects.feature.board)
    implementation(projects.feature.mypage)
    implementation(projects.feature.study)
    implementation(projects.feature.signup)

    implementation(projects.core.ui)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.buildconfig)
    implementation(projects.core.navigation)

    implementation(projects.data.home)
    implementation(projects.data.weather)
    implementation(projects.data.study)
    implementation(projects.data.alert)
    implementation(projects.data.board)
    implementation(projects.data.user)
    implementation(projects.data.login)

    implementation(libs.kakao.common)
    implementation(libs.kakao.login)
    implementation(libs.kakao.auth)

    implementation(libs.naver.oauth)
//    implementation(libs.naver.jdk)
}