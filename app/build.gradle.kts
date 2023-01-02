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

}
