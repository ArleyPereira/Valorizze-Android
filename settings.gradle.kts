pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // CI hardening: some networks intermittently block/throttle repo.maven.apache.org.
        // Adding repo1 as a fallback avoids build failures when that happens.
        maven { url = uri("https://repo1.maven.org/maven2") }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // CI hardening: see pluginManagement.repositories comment above.
        maven { url = uri("https://repo1.maven.org/maven2") }
        mavenCentral()
    }
}

rootProject.name = "Valorizze - Android"
include(":app")

include(":core")
include(":domain")
include(":data")
include(":design")
include(":di")

include(":features:authentication")
 