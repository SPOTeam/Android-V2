plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.home"
}
dependencies {
    implementation(projects.domain.home)
    implementation(projects.domain.weather)
    implementation(projects.domain.study)
    implementation(projects.core.model)
}