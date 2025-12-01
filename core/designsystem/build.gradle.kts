plugins {
    alias(libs.plugins.spot.android.compose.library)
}

android {
    namespace = "com.umcspot.spot.designsystem"
}
dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(libs.flexible.bottomsheet)
    implementation(libs.kizitonwose.calendar.compose)
    implementation(projects.core.common)
    implementation(projects.domain.weather)
    implementation(projects.domain.study)
    implementation(projects.domain.alert)
    implementation(projects.domain.board)
}