plugins {
    alias(libs.plugins.spot.feature)
}

android {
    namespace = "com.umcspot.spot.user"
}

dependencies {
    implementation(projects.domain.token)
    implementation(projects.core.designsystem)
    implementation(projects.domain.user)

    implementation(libs.naver.oauth)
    implementation(libs.androidx.browser) // jdk 17
}