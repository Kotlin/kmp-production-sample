plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.dependencyUpdates).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
}

allprojects {
    // ./gradlew dependencyUpdates
    // Report: build/dependencyUpdates/report.txt
    apply(plugin = "com.github.ben-manes.versions")
}
