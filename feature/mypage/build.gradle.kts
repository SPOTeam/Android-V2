plugins {
    alias(libs.plugins.spot.feature)
}

android {
    namespace = "com.umcspot.spot.mypage"
}

dependencies {
    implementation(projects.domain.study)
    implementation(projects.domain.user)
    implementation(projects.domain.token)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
}