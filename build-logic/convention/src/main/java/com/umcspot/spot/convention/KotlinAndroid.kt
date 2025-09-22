package com.umcspot.spot.convention

import com.android.build.api.dsl.CommonExtension
import com.umcspot.spot.convention.extension.getVersion
import com.umcspot.spot.convention.extension.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply("org.jetbrains.kotlin.android")

    commonExtension.apply {
        compileSdk = libs.getVersion("compileSdk").requiredVersion.toInt()

        defaultConfig {
            minSdk = libs.getVersion("minSdk").requiredVersion.toInt()

            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        extensions.getByType<KotlinAndroidProjectExtension>().configureCompilerOptions()

        buildTypes {
            getByName("debug") {
                proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-debug.pro",
                )
            }

            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro",
                )
            }
        }
    }
}

fun KotlinAndroidProjectExtension.configureCompilerOptions() {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}