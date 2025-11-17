plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.board"
}

dependencies {
    implementation(projects.domain.board)
    implementation(projects.core.designsystem)
}