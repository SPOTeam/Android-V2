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
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
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
include(":feature:jjim")
include(":feature:alert")
include(":data:alert")
include(":domain:alert")
include(":data:board")
include(":feature:study")
include(":feature:signup")
include(":domain:user")
include(":data:user")
include(":data:login")
include(":domain:token")
include(":data:post")
include(":domain:post")
