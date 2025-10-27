plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.user"
}
dependencies {
    implementation(projects.domain.user)
}