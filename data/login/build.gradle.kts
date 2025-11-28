plugins {
    alias(libs.plugins.spot.data)
}

android {
    namespace = "com.umcspot.spot.login"
}
dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(projects.domain.token)

    implementation(libs.datastore.core)
}
