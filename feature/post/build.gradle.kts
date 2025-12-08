plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.post"
}

dependencies {
    implementation(projects.domain.post)
    implementation(projects.feature.board)
    implementation(projects.core.designsystem)
}