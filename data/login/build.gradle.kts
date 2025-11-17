plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.login"
}
dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.domain.token)
}
