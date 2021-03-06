package dev.hitools.noah.modules.account.password.forgot

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.hitools.common.extensions.mainThread
import dev.hitools.common.utils.databinding.bus.Event
import dev.hitools.common.utils.StorageUtils
import dev.hitools.noah.R
import dev.hitools.noah.entries.UserContainer
import dev.hitools.noah.entries.http.AppHttpResponse
import dev.hitools.noah.entries.params.request.ForgotPasswordParams
import dev.hitools.noah.modules.account.common.AppModel
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ForgotPasswordViewModel(application: Application) : AppBaseViewModel(application) {

    private val _verifyCodeStatus = MutableLiveData<Event<Boolean>>()
    val verifyCodeStatus: LiveData<Event<Boolean>>
        get() = _verifyCodeStatus

    private val _resetState = MutableLiveData<Event<Boolean>>()
    val resetState: LiveData<Event<Boolean>>
        get() = _resetState

    @Suppress("UNUSED_PARAMETER")
    fun sendVerifyCode(phone: String) = viewModelScope.launch(Dispatchers.Main) {
        delay(2000)
        val result = Random().nextInt() % 2 == 0
        _verifyCodeStatus.value = Event(result)
        if (!result) toast("发送验证码失败")
    }

    fun resetPassword(phone: String, verifyCode: String, password: String, ensurePassword: String) {
        if (!TextUtils.equals(password, ensurePassword)) {
            toast(R.string.please_input_right_ensure_password)
            return
        }
        val params = ForgotPasswordParams()
        params.phone = phone
        params.code = verifyCode
        params.password = password

        viewModelScope.launch {
            val accountModel = AppModel.instance
            val result: AppHttpResponse<Any> = requestResponse { accountModel.forgotPassword(params) }
            if (result.isSuccess) {
                mainThread { _resetState.value = Event(true) }
                StorageUtils.save(UserContainer.Key.ACCOUNT, phone)
            } else {
                toast(result.message)
            }
        }
    }
}