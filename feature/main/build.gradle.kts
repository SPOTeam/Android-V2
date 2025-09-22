plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.main"
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.mypage)
    implementation(libs.androidx.splashscreen)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.material3.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
}