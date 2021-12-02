plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":sharedui"))
                implementation(project(":shared"))
                //Compose UI
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

// ./gradlew :desktopApp:run
compose.desktop {
    application {
        mainClass = "com.github.jetbrains.rssreader.desktopapp.MainKt"
    }
}