package com.umcspot.spot.convention

import com.android.build.api.dsl.CommonExtension
import com.umcspot.spot.convention.extension.getBundle
import com.umcspot.spot.convention.extension.implementation
import com.umcspot.spot.convention.extension.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKotlinCoroutine(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        dependencies {
            implementation(libs.getBundle("coroutine"))
        }
    }
}