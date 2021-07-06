/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.noah.modules.account.password.forgot

import android.content.Intent
import android.os.Bundle
import android.view.View
import dev.hitools.common.extensions.toast
import dev.hitools.common.utils.databinding.bus.Event
import dev.hitools.common.utils.router.AppRouter
import dev.hitools.common.utils.watcher.EnableTextWatcher
import dev.hitools.common.utils.watcher.VerifyCodeTextWatcher
import dev.hitools.common.utils.watcher.checker.PhoneNumberChecker
import dev.hitools.noah.R
import dev.hitools.noah.databinding.ActivityPasswordBinding
import dev.hitools.noah.modules.account.login.LoginActivity
import dev.hitools.noah.modules.base.mvvm.view.AppBindActivity
import dev.hitools.noah.utils.checker.PasswordChecker

/**
 * Created by yuhaiyang on 2018/8/8.
 * 修改密码和重置密码一系类的东西
 * 和注册分开预防后期业务更改
 */
class ForgotPasswordActivity : AppBindActivity<ActivityPasswordBinding, ForgotPasswordViewModel>() {

    private lateinit var mVerifyCodeWatcher: VerifyCodeTextWatcher
    private lateinit var mSubmitWatcher: EnableTextWatcher

    private lateinit var mViewModel: ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_password)
    }

    override fun initViewModel(vm: ForgotPasswordViewModel) {
        super.initViewModel(vm)
        observeLiveData(vm)
        mViewModel = vm
    }

    override fun initViews() {
        super.initViews()
        mVerifyCodeWatcher = VerifyCodeTextWatcher()
            .setEnableView(binding.sendVerifyCode)
            .addChecker(binding.phone, PhoneNumberChecker())

        mSubmitWatcher = EnableTextWatcher()
            .setEnableView(binding.submit)
            .addChecker(binding.phone, PhoneNumberChecker())
            .addChecker(binding.verifyCode)
            .addChecker(binding.password, PasswordChecker(context))
            .addChecker(binding.ensurePassword, PasswordChecker(context))
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.sendVerifyCode -> {
                binding.sendVerifyCode.showLoading()
                mViewModel.sendVerifyCode(binding.phone.inputText)
            }
            R.id.submit -> {
                mViewModel.resetPassword(
                    binding.phone.inputText,
                    binding.verifyCode.inputText,
                    binding.password.inputText,
                    binding.ensurePassword.inputText
                )
            }
        }
    }

    private fun observeLiveData(vm: ForgotPasswordViewModel) = vm.run {
        verifyCodeStatus.observe(activity, { onVerifyCodeStatusChanged(it) })
        resetState.observe(activity, { resetSuccess() })
    }

    private fun onVerifyCodeStatusChanged(status: Event<Boolean>) {
        status.getContent()?.let { success ->
            if (success) {
                binding.sendVerifyCode.startTiming()
            } else {
                binding.sendVerifyCode.reset()
            }
        }
    }

    private fun resetSuccess() {
        toast(R.string.reset_password_success)
        AppRouter.with(context)
            .target(LoginActivity::class.java)
            .flag(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            .finishSelf()
            .start()
    }
}
