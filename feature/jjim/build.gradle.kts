plugins {
    alias(libs.plugins.spot.feature)
}

android {
    namespace = "com.umcspot.spot.jjim"
}

dependencies {
    implementation(projects.domain.study)
    implementation(projects.core.designsystem)

    implementation(libs.lottie)
    implementation(libs.lottie.compose)
}