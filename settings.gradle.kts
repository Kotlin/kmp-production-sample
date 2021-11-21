rootProject.name = "RssReader"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

include(":androidApp")
include(":webApp")
include(":shared")

