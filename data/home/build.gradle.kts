plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.home"
}
dependencies {
    implementation(projects.domain.home)
}