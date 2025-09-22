plugins {
    alias(libs.plugins.spot.android.compose.library)
}

android {
    namespace = "com.umcspot.spot.ui"
}

dependencies {
    androidTestImplementation(libs.bundles.coil)
}