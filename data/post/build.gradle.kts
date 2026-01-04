plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.post"
}
dependencies {
    implementation(projects.domain.post)
    implementation(projects.core.model)
    implementation(projects.core.network)
}
