package dev.hitools.noah.modules.account.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.hitools.common.extensions.getInteger
import dev.hitools.common.utils.StorageUtils
import dev.hitools.common.utils.StringUtils
import dev.hitools.noah.R
import dev.hitools.noah.entries.UserContainer
import dev.hitools.noah.manager.UserManager
import dev.hitools.noah.modules.account.common.AppModel
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import dev.hitools.noah.utils.http.okhttp.interceptor.AppHttpInterceptor
import kotlinx.coroutines.launch


class LoginViewModel(application: Application) : AppBaseViewModel(application) {
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String>
        get() = _phoneNumber

    private val _passwordHint = MutableLiveData<String>()
    val passwordHint: LiveData<String>
        get() = _passwordHint

    private lateinit var accountModel: AppModel

    override fun init() {
        accountModel = AppModel.instance
        val account = StorageUtils.get(UserContainer.Key.ACCOUNT, StringUtils.EMPTY)
        _phoneNumber.value = account

        val min = context.getInteger(R.integer.min_password)
        val max = context.getInteger(R.integer.max_password)
        _passwordHint.value = context.getString(R.string.login_hint_password, min, max)

        clear()
    }

    /**
     * 登录
     */
    fun login(phone: String, password: String) = viewModelScope.launch {
        accountModel.login(phone, password)

        val result = requestResponse { accountModel.login(phone, password) }
        if (result.isSuccess) {
            saveUserInfo(phone)
            UserManager.instance.setUserContainer(result.data)
            showSuccess()
        } else {
            toast(result.message)
        }
    }

    /**
     * 清除用户缓存
     */
    private fun clear() {
        AppHttpInterceptor.token = null

        StorageUtils.remove(UserContainer.Key.CACHE)
    }

    /**
     * 保存用户信息
     */
    private fun saveUserInfo(account: String) {
        StorageUtils.save(UserContainer.Key.ACCOUNT, account)
    }
}