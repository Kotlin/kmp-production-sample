buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${properties["version.kotlin"]}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${properties["version.kotlin"]}")
        classpath("com.android.tools.build:gradle:${properties["version.androidGradlePlugin"]}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}