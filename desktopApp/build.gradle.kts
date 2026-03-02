import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    dependencies {
        implementation(projects.shared)
        implementation(compose.desktop.currentOs)
        implementation(libs.kotlinx.coroutines.swing)
        implementation(libs.multiplatform.settings)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.core)
    }
}

compose.desktop {
    application {
        mainClass = "com.github.jetbrains.rssreader.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.github.jetbrains.rssreader"
            packageVersion = "1.0.0"
        }
    }
}
