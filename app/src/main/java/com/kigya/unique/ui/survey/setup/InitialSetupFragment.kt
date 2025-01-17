package com.kigya.unique.ui.survey.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.databinding.FragmentInitialSetupBinding
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.utils.extensions.context.onTouchResponseVibrate
import com.kigya.unique.utils.extensions.ui.findTopNavController
import com.kigya.unique.utils.extensions.ui.view.moveToCenter
import com.kigya.unique.utils.extensions.ui.view.playFadeOutAnimation
import com.kigya.unique.utils.extensions.ui.view.scaleUpEffect
import com.kigya.unique.utils.extensions.ui.view.startSidesCircularReveal
import com.kigya.unique.utils.system.thread.ThreadUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialSetupFragment : BaseFragment(R.layout.fragment_initial_setup) {

    private val viewBinding by viewBinding(FragmentInitialSetupBinding::bind)
    override val viewModel by viewModels<InitialSetupViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startSidesCircularReveal(false)
        setStudentClickListener()
        setTeacherClickListener()
    }

    private fun navigateToTabs() {
        viewModel.performAfterDelay {
            ThreadUtil.runOnUiThread {
                findTopNavController().navigate(R.id.action_initialSetupFragment_to_tabsFragment)
            }
        }
    }

    private fun setTeacherClickListener() {
        with(viewBinding) {
            ibTeacher.setOnClickListener {
                context?.onTouchResponseVibrate {
                    performChoiceActions(it, AccountType.TEACHER) {
                        ibStudent.playFadeOutAnimation()
                    }
                }
            }
        }
    }

    private fun setStudentClickListener() {
        with(viewBinding) {
            ibStudent.setOnClickListener {
                performChoiceActions(it, AccountType.STUDENT) {
                    context?.onTouchResponseVibrate {
                        ibTeacher.playFadeOutAnimation()
                    }
                }
            }
        }
    }

    private fun performChoiceActions(it: View, accountType: AccountType, block: () -> Unit = {}) {
        it.scaleUpEffect()
        block()
        it.moveToCenter()
        viewModel.signIn(accountType)
        navigateToTabs()
    }
}
