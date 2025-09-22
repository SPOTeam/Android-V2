plugins {
    alias(libs.plugins.spot.android.compose.library)
}

android {
    namespace = "com.umcspot.spot.designsystem"
}
dependencies {
    implementation(projects.core.ui)
    implementation(libs.flexible.bottomsheet)
}