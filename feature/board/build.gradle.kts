plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.board"
}

dependencies {
    implementation(projects.domain.board)
    implementation(projects.domain.post)
    implementation(projects.core.designsystem)
}