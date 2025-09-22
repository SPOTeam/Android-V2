import com.umcspot.spot.convention.extension.getLibrary
import com.umcspot.spot.convention.extension.implementation
import com.umcspot.spot.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SpotDataPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("spot.android.library")
                apply("spot.android.hilt")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                implementation(project(":core:network"))
                implementation(project(":core:common"))
                implementation(project(":core:model"))

                implementation(libs.getLibrary("kotlinx.serialization.json"))
                implementation(libs.getLibrary("retrofit.core"))
                implementation(libs.getLibrary("retrofit.kotlin.serialization"))
            }
        }
    }
}