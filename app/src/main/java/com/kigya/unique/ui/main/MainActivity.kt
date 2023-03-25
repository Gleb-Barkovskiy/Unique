package com.kigya.unique.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.kigya.unique.R
import com.kigya.unique.ui.timetable.sheet.student.DialogStudentFragment
import com.kigya.unique.ui.timetable.sheet.teacher.DialogTeacherFragment
import com.kigya.unique.utils.system.intent.IntentCreator
import dagger.hilt.android.AndroidEntryPoint
import shortbread.Shortcut

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()
    private var navController: NavController? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?,
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreen()
        setContentView(R.layout.activity_main)
        setupWindow()

        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
        onNavControllerActivated(navController)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
        onBackPressedActivate(navController)
    }

    private fun setupWindow() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    private fun setupSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Shortcut(
        id = TEACHER_SHORTCUT_ID,
        icon = R.drawable.teacher_icon_shortcut,
        shortLabelRes = R.string.teacher_mode,
        rank = 1,
        disabledMessageRes = R.string.unavailable,
    )
    fun openTeacherIntent() {
        if (viewModel.isUserSignedIn) {
            IntentCreator.createRestartIntentWithOpeningDialog(
                this,
                MainActivity::class.java,
                DialogTeacherFragment.EXTRA,
            )
            viewModel.logInToTeacherMode()
        } else {
            Toast.makeText(this, "Необходимо сперва войти", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Shortcut(
        id = STUDENT_SHORTCUT_ID,
        icon = R.drawable.student_icon_shortcut,
        shortLabelRes = R.string.student_mode,
        rank = 2,
        disabledMessageRes = R.string.unavailable,
    )
    fun openStudentIntent() {
        if (viewModel.isUserSignedIn) {
            IntentCreator.createRestartIntentWithOpeningDialog(
                this,
                MainActivity::class.java,
                DialogStudentFragment.EXTRA,
            )
            viewModel.logInToStudentMode()
        } else {
            Toast.makeText(this, "Необходимо сперва войти", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    private fun onBackPressedActivate(navController: NavController) {
        onBackPressed(true) {
            if (navController.currentDestination?.id == R.id.initialSetupFragment ||
                navController.currentDestination?.id == R.id.tabsFragment
            ) {
                finish()
            } else {
                navController.popBackStack()
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun onBackPressed(isEnabled: Boolean, callback: () -> Unit) {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(isEnabled) {
                override fun handleOnBackPressed() {
                    callback()
                }
            },
        )
    }

    override fun onSupportNavigateUp(): Boolean =
        (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
        graph.setStartDestination(
            if (isSignedIn) getTabsDestination() else getOnboardingDestination(),
        )
        navController.graph = graph
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController = navController
    }

    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    private fun isSignedIn() = viewModel.isUserSignedIn

    @Suppress("SameReturnValue")
    private fun getMainNavigationGraphId() = R.navigation.main_graph

    @Suppress("SameReturnValue")
    private fun getTabsDestination() = R.id.tabsFragment

    @Suppress("SameReturnValue")
    private fun getOnboardingDestination() = R.id.onboardingFragment

    companion object {
        const val TEACHER_SHORTCUT_ID = "open_teacher"
        const val STUDENT_SHORTCUT_ID = "open_student"
    }
}
