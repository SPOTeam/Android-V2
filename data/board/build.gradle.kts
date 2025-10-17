plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.board"
}
dependencies {
    implementation(projects.domain.board)
    implementation(projects.core.model)
}