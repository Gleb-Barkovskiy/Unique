package com.kigya.unique.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.databinding.FragmentSettingsBinding
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.utils.extensions.ui.view.setOnSidesSwipeTouchListener
import com.kigya.unique.utils.extensions.ui.view.startCenterCircularReveal
import com.kigya.unique.utils.system.intent.IntentCreator
import dagger.hilt.android.AndroidEntryPoint
import io.ghyeok.stickyswitch.widget.StickySwitch

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewBinding by viewBinding(FragmentSettingsBinding::bind)
    override val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startCenterCircularReveal()
        setBackButtonClickListener()
        setOnTgButtonClickListener()
        setOnMailClickListener()
        setInitialViewState()
        setSaveButtonClickListener()
        setOnRightSwipeListener()
    }

    private fun setOnRightSwipeListener() {
        viewBinding.root.setOnSidesSwipeTouchListener(
            rightAction = {
                findNavController()
                    .navigate(SettingsFragmentDirections.actionSettingsFragmentToTabsFragment())
            },
        )
    }

    private fun setSaveButtonClickListener() {
        viewBinding.btnSave.setOnClickListener {
            viewModel.setListAnimationEnabled(
                viewBinding.ssListAnimation.getDirection() == StickySwitch.Direction.RIGHT,
            )
            findNavController()
                .navigate(SettingsFragmentDirections.actionSettingsFragmentToTabsFragment())
        }
    }

    private fun setInitialViewState() {
        viewBinding.ssListAnimation.setDirection(
            if (viewModel.isAnimationEnabled()) {
                StickySwitch.Direction.RIGHT
            } else {
                StickySwitch.Direction.LEFT
            },
        )
    }

    private fun setOnMailClickListener() {
        viewBinding.ibMail.setOnClickListener {
            IntentCreator.sendEmail(EMAIL)
        }
    }

    private fun setOnTgButtonClickListener() {
        viewBinding.ibTelegram.setOnClickListener {
            IntentCreator.createViewIntentByLink(TELEGRAM_URI)
        }
    }

    private fun setBackButtonClickListener() {
        viewBinding.btnBack.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToTabsFragment())
        }
    }

    companion object {
        const val TELEGRAM_URI = "https://t.me/kigya"
        const val EMAIL = "kirillborichevskiy@gmail.com"
    }
}
