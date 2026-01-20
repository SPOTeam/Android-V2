plugins {
    alias(libs.plugins.spot.android.java.library)
}


dependencies {
    implementation(projects.core.model)
    implementation(libs.bundles.coroutine)
}