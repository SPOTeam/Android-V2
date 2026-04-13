plugins {
    alias(libs.plugins.spot.android.library)
}

android {
    namespace = "com.umcspot.spot.navigation"
}

dependencies {
    implementation(libs.navigation.compose)
}