import com.umcspot.spot.convention.extension.getBundle
import com.umcspot.spot.convention.extension.getLibrary
import com.umcspot.spot.convention.extension.implementation
import com.umcspot.spot.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SpotFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("spot.android.compose.library")
                apply("spot.android.hilt")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                implementation(libs.getLibrary("kotlinx.serialization.json"))
                implementation(project(":core:ui"))
                implementation(project(":core:designsystem"))
                implementation(project(":core:model"))
                implementation(project(":core:navigation"))
                implementation(libs.getBundle("compose"))
            }
        }
    }
}