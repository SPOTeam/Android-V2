plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.weather"
}
dependencies {
    implementation(projects.domain.weather)
}