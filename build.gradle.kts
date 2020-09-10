buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${properties["version.kotlin"]}")
        classpath("com.android.tools.build:gradle:${properties["version.androidGradlePlugin"]}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}
