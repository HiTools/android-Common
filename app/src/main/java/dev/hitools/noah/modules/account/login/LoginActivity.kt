/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.noah.modules.account.login

import android.os.Bundle
import android.view.View
import dev.hitools.common.entries.status.Success
import dev.hitools.common.extensions.open
import dev.hitools.common.utils.watcher.EnableTextWatcher
import dev.hitools.common.utils.watcher.checker.PhoneNumberChecker
import dev.hitools.noah.R
import dev.hitools.noah.databinding.ActivityLoginBinding
import dev.hitools.noah.modules.account.password.forgot.ForgotPasswordActivity
import dev.hitools.noah.modules.account.register.RegisterActivity
import dev.hitools.noah.modules.base.mvvm.view.AppBindActivity
import dev.hitools.noah.modules.main.index.MainActivity
import dev.hitools.noah.utils.checker.PasswordChecker


/**
 * Created by yuhaiyang on 2018/8/8.
 * 登录界面
 */
class LoginActivity : AppBindActivity<ActivityLoginBinding, LoginViewModel>() {

    private var enableWatcher = EnableTextWatcher()
    private var type = TYPE_FINISHED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_login)
    }

    override fun initViews() {
        super.initViews()
        enableWatcher.setEnableView(binding.login)
            .addChecker(binding.account, PhoneNumberChecker())
            .addChecker(binding.password, PasswordChecker(context))
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        type = intent.getIntExtra(KEY_TYPE, TYPE_FINISHED)
    }

    fun onViewClick(v: View) {
        when (v.id) {

            R.id.login -> {
                vm?.login(binding.account.inputText, binding.password.inputText)
            }

            R.id.register -> {
                open(RegisterActivity::class.java)
            }

            R.id.forgotPassword -> {
                open(ForgotPasswordActivity::class.java)
            }
        }
    }

    override fun showSuccess(success: Success) {
        super.showSuccess(success)
        onBackPressed()
    }

    override fun onBackPressed() {
        if (type == TYPE_FINISHED) {
            finish()
        } else {
            open(MainActivity::class.java, true)
        }
    }

    companion object {
        /**
         * 跳转首页
         */
        const val TYPE_GOTO_MAIN = 1
        /**
         * 回退
         */
        const val TYPE_FINISHED = 2
    }
}
