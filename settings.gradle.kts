rootProject.name = "RssReader"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

include(":shared")
include(":composeApp")
include(":webApp")

enableFeaturePreview("VERSION_CATALOGS")