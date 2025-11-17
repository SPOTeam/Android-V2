plugins {
    alias(libs.plugins.spot.android.library)
    alias(libs.plugins.spot.android.build.config)
    alias(libs.plugins.spot.android.hilt)
    alias(libs.plugins.spot.android.test)
}

android {
    namespace = "com.umcspot.spot.buildconfig"

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://api-spot.site/\"")
    }

}


dependencies {
    implementation(projects.core.common)
}