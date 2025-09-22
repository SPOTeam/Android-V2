plugins {
    alias(libs.plugins.spot.android.library)
    alias(libs.plugins.spot.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spot.android.test)
}

android {
    namespace = "com.umcspot.spot.network"
}
dependencies {
    implementation(libs.bundles.datastore)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.process.phoenix)
}