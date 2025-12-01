plugins {
    alias(libs.plugins.spot.android.library)
    alias(libs.plugins.spot.android.build.config)
    alias(libs.plugins.spot.android.hilt)
    alias(libs.plugins.spot.android.test)
}

android {
    namespace = "com.umcspot.spot.buildconfig"
}


dependencies {
    implementation(projects.core.common)
}