plugins {
    alias(libs.plugins.spot.android.java.library)
    alias(libs.plugins.kotlin.serialization)
}
dependencies {
    implementation(libs.kotlinx.serialization.json)
}