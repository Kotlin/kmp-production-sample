buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("version.kotlin")}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${findProperty("version.kotlin")}")
        classpath("com.android.tools.build:gradle:${findProperty("version.androidGradlePlugin")}")
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.0.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}