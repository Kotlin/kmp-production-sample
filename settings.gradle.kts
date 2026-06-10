enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "RssReader"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        val kotlin_repo_url: String? by settings
        val kotlin_version: String? by settings
        val repoUrl = kotlin_repo_url
        val kotlinVersion = kotlin_version
        if (!repoUrl.isNullOrEmpty() && !kotlinVersion.isNullOrEmpty()) {
            exclusiveContent {
                forRepository {
                    maven(url = repoUrl)
                }
                filter {
                    includeVersionByRegex("org\\.jetbrains\\.kotlin(\\..*)?", ".*", kotlinVersion)
                }
            }
        }
    }
//    plugins {
//        kotlin("jvm") version "2.2.0"
//    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val kotlin_version: String? by settings
            kotlin_version?.let { version("kotlin", it) }
        }
    }
    repositories {
        google()
        mavenCentral()
        val kotlin_repo_url: String? by settings
        val kotlin_version: String? by settings
        val repoUrl = kotlin_repo_url
        val kotlinVersion = kotlin_version
        if (!repoUrl.isNullOrEmpty() && !kotlinVersion.isNullOrEmpty()) {
            exclusiveContent {
                forRepository {
                    maven(url = repoUrl)
                }
                filter {
                    includeVersionByRegex("org\\.jetbrains\\.kotlin(\\..*)?", ".*", kotlinVersion)
                }
            }
        }
    }
}

include(":composeApp")
include(":shared")