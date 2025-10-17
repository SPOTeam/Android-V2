plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.alert"
}

dependencies {
    implementation(projects.domain.alert)
    implementation(projects.core.designsystem)
}