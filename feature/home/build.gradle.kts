plugins {
    alias(libs.plugins.spot.feature)
}
android {
    namespace = "com.umcspot.spot.home"
}

dependencies {
    implementation(projects.domain.weather)
    implementation(projects.domain.board)
    implementation(projects.domain.study)
    implementation(projects.core.designsystem)
    implementation(libs.google.location)
    implementation(libs.kotlinx.coroutines.play.services)
}