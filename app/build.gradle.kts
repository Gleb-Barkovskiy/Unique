import supplier.Supplier
import extensions.*
import params.*

plugins {
    kotlin("android")
    kotlin("kapt")
    with(subjections.Plugins) {
        id(application)
        id(safeargs)
        id(hilt)
        id(ktLint) version params.Versions.ktLint
        id("kotlin-parcelize")
    }
}
@Suppress("UnstableApiUsage")
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
        JavaVersion.VERSION_1_8.apply {
            sourceCompatibility = this
            targetCompatibility = this
        }
    }

    kotlinOptions.jvmTarget = params.Versions.jvmVersion

    viewBinding.enable = true

    namespace = params.AppConfig.id
}

dependencies {
    with(Supplier) {
        implementation(implementationList)
        kapt(kaptList)
        testImplementation(testImplementationList)
        androidTestImplementation(androidTestImplementationList)
    }

    implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.github.Spikeysanju:MotionToast:1.4")
    implementation("androidx.activity:activity:1.7.0-alpha03")
    implementation("com.nex3z:toggle-button-group:1.2.3")

}
