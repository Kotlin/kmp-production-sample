rootProject.name = "RssReader"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

include(":androidApp")
include(":shared")
include(":sharedui")
include(":desktopApp")
