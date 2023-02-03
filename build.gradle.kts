buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0-alpha02")
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


tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}