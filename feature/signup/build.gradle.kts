plugins {
    alias(libs.plugins.spot.feature)
}

android {
    namespace = "com.umcspot.spot.user"
}

dependencies {
    implementation(projects.domain.token)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.domain.user)

    implementation(libs.naver.oauth)
    implementation(libs.androidx.browser) // jdk 17

    implementation(libs.kakao.login)
    implementation(libs.kakao.auth)
    implementation(libs.kakao.common)

    implementation(libs.naver.oauth)
//    implementation(libs.naver.jdk)
}