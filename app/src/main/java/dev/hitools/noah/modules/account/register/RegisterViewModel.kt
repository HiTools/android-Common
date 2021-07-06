package dev.hitools.noah.modules.account.register

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.hitools.common.utils.databinding.bus.Event
import dev.hitools.noah.R
import dev.hitools.noah.entries.UserContainer
import dev.hitools.noah.entries.http.AppHttpResponse
import dev.hitools.noah.entries.params.request.RegisterParams
import dev.hitools.noah.manager.UserManager
import dev.hitools.noah.modules.account.common.AppModel
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class RegisterViewModel(application: Application) : AppBaseViewModel(application) {

    private val _verifyCodeStatus = MutableLiveData<Event<Boolean>>()
    val verifyCodeStatus: LiveData<Event<Boolean>>
        get() = _verifyCodeStatus

    @Suppress("UNUSED_PARAMETER")
    fun sendVerifyCode(phone: String) = viewModelScope.launch(Dispatchers.Main) {
        delay(2000)
        val result = Random().nextInt() % 2 == 0
        _verifyCodeStatus.value = Event(result)
        if (!result) toast("发送验证码失败")
    }

    fun register(phone: String, verifyCode: String, password: String, ensurePassword: String) {
        if (!TextUtils.equals(password, ensurePassword)) {
            toast(R.string.please_input_right_ensure_password)
            return
        }
        val params = RegisterParams()
        params.phone = phone
        params.verifyCode = verifyCode
        params.password = password

        viewModelScope.launch {
            val accountModel = AppModel.instance
            val result: AppHttpResponse<UserContainer> = requestResponse { accountModel.register(params) }
            if (result.isSuccess) {
                UserManager.instance.setUserContainer(result.data)
                showSuccess()
            } else {
                toast(result.message)
            }
        }

    }
}