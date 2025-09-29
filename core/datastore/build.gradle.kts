plugins {
    alias(libs.plugins.spot.android.library)
    alias(libs.plugins.spot.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spot.android.test)
}

android {
    namespace = "com.umcspot.spot.datastore"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.datastore)
}