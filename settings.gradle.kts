enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SPOT"
include(":app")
include(":core:designsystem")
include(":core:buildconfig")
include(":core:common")
include(":core:model")
include(":core:navigation")
include(":core:network")
include(":data:home")
include(":domain:home")
include(":feature:home")
include(":core:ui")
include(":feature:main")
include(":feature:mypage")
include(":core:datastore")
include(":domain:weather")
include(":domain:study")
include(":feature:board")
include(":domain:board")
include(":data:weather")
include(":data:study")
include(":feature:category")
include(":feature:mystudy")
include(":feature:jjim")
