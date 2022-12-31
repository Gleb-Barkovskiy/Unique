plugins {
    id(Plugins.application)
    kotlin("android")
    kotlin("kapt")
    id(Plugins.safeargs)
    id(Plugins.hilt)
    id(Plugins.ktLint) version Versions.ktLint
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.id
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
        signingConfig = signingConfigs.getByName("debug")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = Versions.jvmVersion

    viewBinding.enable = true

    namespace = AppConfig.id
}

dependencies {
    testImplementation(AppDependencies.JUnit.junit)
    androidTestImplementation(AppDependencies.JUnit.ext)

    androidTestImplementation(AppDependencies.Espresso.core)

    implementation(AppDependencies.AppCompat.appcompat)
    implementation(AppDependencies.Core.ktx)
    implementation(AppDependencies.Material.material)

    implementation(AppDependencies.Constraint.constraintLayout)

    implementation(AppDependencies.Jsoup.jsoup)

    implementation(AppDependencies.Navigation.ktx)
    implementation(AppDependencies.Navigation.uiKtx)

    implementation(AppDependencies.Coroutines.core)
    implementation(AppDependencies.Coroutines.android)

    implementation(AppDependencies.Lifecycle.ktx)
    implementation(AppDependencies.Lifecycle.runtime)

    implementation(AppDependencies.Hilt.hilt)
    annotationProcessor(AppDependencies.Hilt.compiler)
    kapt(AppDependencies.Hilt.compiler)

    implementation(AppDependencies.AnimatedBottomBar.library)

    implementation(AppDependencies.Room.runtime)
    implementation(AppDependencies.Room.ktx)
    kapt(AppDependencies.Room.compiler)

    implementation(AppDependencies.LiveData.ktx)

    implementation(AppDependencies.PowerSpinner.powerSpinner)

    implementation(AppDependencies.ToggleButtonGroup.toggleButtonGroup)

    implementation(AppDependencies.SurroundCardView.surroundCardView)

    implementation(AppDependencies.RoundCornerProgressBar.roundCornerProgressBar)

    implementation(AppDependencies.RecyclerViewAnimators.recyclerViewAnimators)

    implementation(AppDependencies.ViewBindingPropertyDelegate.viewBindingPropertyDelegate)

    implementation(AppDependencies.DataStore.preferences)

    implementation(AppDependencies.Gson.gson)

    implementation(AppDependencies.Lottie.lottie)

    implementation(AppDependencies.Dimens.ssp)

    implementation(AppDependencies.CircleProgressBar.progressBar)

    implementation("com.kizitonwose.calendar:view:2.0.4")

    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.github.amarjain07:StickyScrollView:1.0.3")

    implementation("androidx.core:core-splashscreen:1.0.0")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.fragment:fragment-ktx:1.5.5")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    implementation("io.arrow-kt:arrow-core:1.0.1")
}