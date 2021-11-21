plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

repositories {
    maven(url = "https://dl.bintray.com/kotlin/kotlin-js-wrappers")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation(project(":shared"))
}

kotlin {
    js(IR) {
        browser {
            webpackTask {
                output.libraryTarget = "commonjs2"
            }
        }
        binaries.executable()
    }
}
