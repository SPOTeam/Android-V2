plugins {
    alias(libs.plugins.spot.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.spot.android.hilt)
}

android {
    namespace = "com.umcspot.spot"

//    signingConfigs {
//        getByName("debug") {
//            keyAlias = "androiddebugkey"
//            keyPassword = "android"
//            storeFile = file("debug.keystore")
//        }
//    }

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
}