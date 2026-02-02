plugins {
    alias(libs.plugins.spot.feature)
}

android {
    namespace = "com.umcspot.spot.study"
}

dependencies {
    implementation(projects.domain.study)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(libs.kizitonwose.calendar.compose)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.material3.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
}
