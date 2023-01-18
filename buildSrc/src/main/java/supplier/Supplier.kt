package supplier

import subjections.AppDependencies

object Supplier {
    val implementationList = listOf(
        AppDependencies.JUnit.junit,
        AppDependencies.AppCompat.appcompat,
        AppDependencies.Core.ktx,
        AppDependencies.Material.material,
        AppDependencies.Constraint.constraintLayout,
        AppDependencies.AnimatedBottomBar.library,
        AppDependencies.PowerSpinner.powerSpinner,
        AppDependencies.ToggleButtonGroup.toggleButtonGroup,
        AppDependencies.RecyclerViewAnimators.recyclerViewAnimators,
        AppDependencies.Lottie.lottie,
        AppDependencies.CircleProgressBar.progressBar,
        AppDependencies.Calendar.calendarView,
        AppDependencies.CardView.cardView,
        AppDependencies.SplashScreen.splashScreen,
        AppDependencies.Jsoup.jsoup,
        AppDependencies.ViewBindingPropertyDelegate.viewBindingPropertyDelegate,
        AppDependencies.Navigation.ktx,
        AppDependencies.Navigation.uiKtx,
        AppDependencies.Coroutines.core,
        AppDependencies.Coroutines.android,
        AppDependencies.Lifecycle.ktx,
        AppDependencies.Lifecycle.runtime,
        AppDependencies.Lifecycle.viewModel,
        AppDependencies.Lifecycle.extensions,
        AppDependencies.Hilt.hilt,
        AppDependencies.Room.runtime,
        AppDependencies.Room.ktx,
        AppDependencies.DataStore.preferences,
        AppDependencies.Gson.gson,
        AppDependencies.Dimens.ssp,
        AppDependencies.Fragment.fragment,
        AppDependencies.RecyclerView.recyclerView,
        AppDependencies.Retrofit.retrofit,
        AppDependencies.OkHttp.loggingInterceptor
    )

    val testImplementationList = listOf(
        AppDependencies.JUnit.junit
    )

    val androidTestImplementationList = listOf(
        AppDependencies.JUnit.ext,
        AppDependencies.Espresso.core
    )

    val annotationProcessorList = listOf(
        AppDependencies.Hilt.compiler
    )

    val kaptList = listOf(
        AppDependencies.Room.compiler,
        AppDependencies.Hilt.compiler
    )
}