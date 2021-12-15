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

enableFeaturePreview("VERSION_CATALOGS")