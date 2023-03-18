buildscript {
    val kotlin_version by extra("1.8.20-Beta")
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0-alpha06")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.github.matthiasrobbers:shortbread-gradle-plugin:1.4.0")
    }
    repositories {
        mavenCentral()
    }
}
plugins {
    with(params.Versions) {
        id(subjections.Plugins.application) version application apply false
        id(subjections.Plugins.androidLibrary) version application apply false
        id(subjections.Plugins.kotlinAndroid) version kotlin apply false
        id(subjections.Plugins.safeargs) version safeargs apply false
        id(subjections.Plugins.hiltGoogle) version hiltPlugin apply false
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
