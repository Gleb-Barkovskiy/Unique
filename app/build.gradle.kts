import supplier.Supplier
import extensions.*
import params.*

plugins {
    kotlin("android")
    with(subjections.Plugins) {
        id(application)
        id(safeargs)
        id(hilt)
        id(ktLint) version params.Versions.ktLint
        id("kotlin-parcelize")
    }
    kotlin("kapt")
}
android {
    with(AppConfig) {
        this@android.compileSdk = this@with.compileSdk

        defaultConfig {
            applicationId = this@with.id
            minSdk = this@with.minSdk
            targetSdk = this@with.targetSdk
            versionCode = this@with.versionCode
            versionName = this@with.versionName
            testInstrumentationRunner = this@with.testInstrumentationRunner
            signingConfig = signingConfigs.getByName("debug")
        }

        flavorDimensions.add("type")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        JavaVersion.VERSION_17.apply {
            sourceCompatibility = this
            targetCompatibility = this
        }
    }

    kotlinOptions.jvmTarget = params.Versions.jvmVersion

    viewBinding.enable = true

    namespace = params.AppConfig.id
}

dependencies {
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    with(Supplier) {
        implementation(implementationList)
        kapt(kaptList)
        testImplementation(testImplementationList)
        androidTestImplementation(androidTestImplementationList)
    }

    implementation("androidx.startup:startup-runtime:1.1.1")


}
