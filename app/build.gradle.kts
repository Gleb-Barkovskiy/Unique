import extensions.*
import params.*
import supplier.Supplier

plugins {
    kotlin("android")
    with(subjections.Plugins) {
        id(application)
        id(safeargs)
        id(hilt)
        id(ktLint) version params.Versions.ktLint
        id("kotlin-parcelize")
        id("com.github.matthiasrobbers.shortbread")
    }
    kotlin("kapt")
}
android {
    lint {
        baseline = file("lint-baseline.xml")
    }
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
                "proguard-rules.pro",
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    with(Supplier) {
        implementation(implementationList)
        kapt(kaptList)
        testImplementation(testImplementationList)
        androidTestImplementation(androidTestImplementationList)
        implementation("com.github.GwonHyeok:StickySwitch:0.0.16")
        implementation("com.github.matthiasrobbers:shortbread:1.4.0")
        kapt("com.github.matthiasrobbers:shortbread-compiler:1.4.0")
    }
}
