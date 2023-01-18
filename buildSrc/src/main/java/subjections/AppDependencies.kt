package subjections

import params.Versions

object AppDependencies {

    // test libraries

    object JUnit {
        const val junit = "junit:junit:${Versions.junit}"
        const val ext = "androidx.test.ext:junit:${Versions.extJunit}"
    }

    object Espresso {
        const val core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    // android ui libraries

    object AppCompat {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    }

    object Core {
        const val ktx = "androidx.core:core-ktx:${Versions.coreKtx}"
    }

    object Material {
        const val material = "com.google.android.material:material:${Versions.material}"
    }

    object Constraint {
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
    }

    object AnimatedBottomBar {
        const val library =
            "nl.joery.animatedbottombar:library:${Versions.animatedBottomBar}"
    }

    object PowerSpinner {
        const val powerSpinner = "com.github.skydoves:powerspinner:${Versions.powerSpinner}"
    }

    object ToggleButtonGroup {
        const val toggleButtonGroup =
            "com.nex3z:toggle-button-group:${Versions.toggleButtonGroup}"
    }

    object RecyclerViewAnimators {
        const val recyclerViewAnimators =
            "jp.wasabeef:recyclerview-animators:${Versions.recyclerViewAnimators}"
    }

    object RecyclerView {
        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    }

    object Lottie {
        const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    }

    object CircleProgressBar {
        const val progressBar = "com.mikhaellopez:circularprogressbar:${Versions.progressBar}"
    }

    object Calendar {
        const val calendarView = "com.kizitonwose.calendar:view:${Versions.calendarView}"
    }

    object CardView {
        const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    }

    object SplashScreen {
        const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"

    }


    // external libraries

    object Jsoup {
        const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"
    }

    object ViewBindingPropertyDelegate {
        const val viewBindingPropertyDelegate =
            "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.viewBindingPropertyDelegate}"
    }

    object Navigation {
        const val ktx =
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    }

    object Coroutines {
        const val core =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Lifecycle {
        const val ktx =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val extensions =
            "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    }

    object Hilt {
        const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
    }

    object DataStore {
        const val preferences =
            "androidx.datastore:datastore-preferences:${Versions.dataStore}"
    }

    object Gson {
        const val gson = "com.google.code.gson:gson:${Versions.gson}"
    }

    object Dimens {
        const val ssp = "com.intuit.ssp:ssp-android:${Versions.ssp}"
    }

    object Fragment {
        const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    }

    object OkHttp {
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    }
}





