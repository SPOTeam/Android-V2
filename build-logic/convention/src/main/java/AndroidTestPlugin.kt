import com.android.build.api.dsl.LibraryExtension
import com.umcspot.spot.convention.extension.androidTestImplementation
import com.umcspot.spot.convention.extension.debugImplementation
import com.umcspot.spot.convention.extension.getLibrary
import com.umcspot.spot.convention.extension.implementation
import com.umcspot.spot.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                androidTestImplementation(libs.getLibrary("androidx.test.runner"))
                debugImplementation(libs.getLibrary("androidx.test.core"))
                androidTestImplementation(libs.getLibrary("kotlinx.coroutines.test"))
                implementation(libs.getLibrary("androidx.test.ext.junit"))
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
        }
    }
}