plugins {
    alias(libs.plugins.spot.android.library)
    alias(libs.plugins.spot.android.hilt)
    alias(libs.plugins.spot.android.test)
}

android {
    namespace = "com.umcspot.spot.common"
}