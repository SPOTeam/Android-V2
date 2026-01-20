plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.main"
}

dependencies {
    //start destination
    implementation(projects.feature.signup)
    implementation(projects.feature.home)
    implementation(projects.feature.category)
    implementation(projects.feature.jjim)
    implementation(projects.feature.mypage)
    implementation(projects.feature.study)
    implementation(projects.feature.board)
    implementation(projects.feature.alert)

    implementation(projects.core.model)

    implementation(libs.androidx.splashscreen)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.material3.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
}