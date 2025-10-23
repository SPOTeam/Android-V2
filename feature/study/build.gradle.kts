plugins {
    alias(libs.plugins.spot.feature)
}

android {
    namespace = "com.umcspot.spot.study"
}

dependencies {
    implementation(projects.domain.study)
    implementation(projects.core.designsystem)
}