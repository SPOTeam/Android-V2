plugins {
    `kotlin-dsl`
}
group = "com.umcspot.spot.convention"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.extension)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "spot.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("buildConfig") {
            id = "spot.android.build.config"
            implementationClass = "BuildConfigPlugin"
        }
        register("androidLibrary") {
            id = "spot.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidComposeLibrary") {
            id = "spot.android.compose.library"
            implementationClass = "AndroidComposeLibraryPlugin"
        }
        register("javaLibrary") {
            id = "spot.android.java.library"
            implementationClass = "JavaLibraryPlugin"
        }
        register("androidTest") {
            id = "spot.android.test"
            implementationClass = "AndroidTestPlugin"
        }
        register("unitTest") {
            id = "spot.android.unittest"
            implementationClass = "UnitTestPlugin"
        }
        register("androidHilt") {
            id = "spot.android.hilt"
            implementationClass = "HiltPlugin"
        }
        register("spotFeature") {
            id = "spot.feature"
            implementationClass = "SpotFeaturePlugin"
        }
        register("spotData") {
            id = "spot.data"
            implementationClass = "SpotDataPlugin"
        }
    }
}