plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.home"
}

dependencies {
    implementation(projects.domain.home)
}