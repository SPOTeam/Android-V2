plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.study"
}
dependencies {
    implementation(projects.domain.study)
    implementation(projects.core.ui)
}