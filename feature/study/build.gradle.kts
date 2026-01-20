plugins {
    alias(libs.plugins.spot.feature)
}

android {
    namespace = "com.umcspot.spot.study"
}

dependencies {
    implementation(projects.domain.study)
    implementation(projects.domain.user)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)

    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.material3.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
}
