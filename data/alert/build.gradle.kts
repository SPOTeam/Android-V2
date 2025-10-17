plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.alert"
}
dependencies {
    implementation(projects.domain.alert)
}