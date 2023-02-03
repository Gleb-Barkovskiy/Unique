package com.kigya.unique.ui.survey.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.databinding.FragmentOnboardingBinding
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.ui.survey.onboarding.OnboardingFragment.Companion.OnboardingConst.Progress.MAX_PROGRESS
import com.kigya.unique.ui.survey.onboarding.OnboardingFragment.Companion.OnboardingConst.Progress.PROGRESS_WAITING_TIME
import com.kigya.unique.ui.survey.onboarding.OnboardingFragment.Companion.OnboardingConst.UI_WAITING_TIME
import com.kigya.unique.utils.extensions.context.onTouchResponseVibrate
import com.kigya.unique.utils.extensions.ui.collectFlow
import com.kigya.unique.utils.extensions.ui.view.fadeInAnimation
import com.kigya.unique.utils.extensions.ui.view.fadeOutAnimation
import com.kigya.unique.utils.extensions.ui.view.findLocationOfCenterOnTheScreen
import com.kigya.unique.utils.extensions.ui.view.preventDisappearing
import com.kigya.unique.utils.extensions.ui.view.setDrawableAnimated
import com.kigya.unique.utils.extensions.ui.view.setOnLeftSwipeTouchListener
import com.kigya.unique.utils.extensions.ui.view.startSidesCircularReveal
import com.kigya.unique.utils.thread.ThreadUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding) {

    private val viewBinding by viewBinding(FragmentOnboardingBinding::bind)
    override val viewModel by viewModels<OnboardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startSidesCircularReveal(false)
        with(viewBinding) {
            setInitialState()
            startProgress()
            handleUserGesture()
            handleDelayAction()
            lottieManAnimation.preventDisappearing()
            onButtonNextClicked()
        }
    }

    private fun FragmentOnboardingBinding.onButtonNextClicked() {
        btnNext.setOnClickListener {
            if (viewModel.isReady) {
                context?.onTouchResponseVibrate {
                    it.findLocationOfCenterOnTheScreen()
                    findNavController().navigate(R.id.action_onboardingFragment_to_initialSetupFragment)
                }
            }
        }
    }

    private fun FragmentOnboardingBinding.startProgress() {
        circularProgressBar.setProgressWithAnimation(MAX_PROGRESS, UI_WAITING_TIME)
    }

    private fun FragmentOnboardingBinding.setInitialState() {
        collectFlow(viewModel.retainer) {
            viewModel.handleIfTriggered(it) {
                changeUiState()
            }
        }
    }

    private fun FragmentOnboardingBinding.handleDelayAction() {
        viewModel.performAfterDelay {
            ThreadUtil.runOnUiThread {
                viewModel.onUiTriggered {
                    changeUiState()
                }
            }
        }
    }

    private fun FragmentOnboardingBinding.handleUserGesture() {
        activity?.let {
            main.setOnLeftSwipeTouchListener {
                with(viewModel) {
                    handleIfPending {
                        onUiTriggered {
                            changeUiState()
                        }
                    }
                }
            }
        }
    }

    private fun FragmentOnboardingBinding.changeUiState() {
        context?.onTouchResponseVibrate {
            lottieSwipeAnimation.fadeOutAnimation()
            lottieSwipeAnimation.pauseAnimation()
            tvCinemaAnswer.run {
                postDelayed(
                    this::fadeInAnimation,
                    OnboardingConst.POST_DELAYED_TIME
                )
            }
            btnNext.setDrawableAnimated(drawable = NEXT_BUTTON_ACTIVE_DRAWABLE)
            circularProgressBar.setProgressWithAnimation(MAX_PROGRESS, PROGRESS_WAITING_TIME)
        }
    }

    companion object {
        @JvmStatic
        private val NEXT_BUTTON_ACTIVE_DRAWABLE = R.drawable.button_next_active

        internal object OnboardingConst {
            const val UI_WAITING_TIME = 10000L
            const val POST_DELAYED_TIME = 200L

            object Progress {
                const val MAX_PROGRESS = 100f
                const val PROGRESS_WAITING_TIME = 500L
            }
        }
    }

}