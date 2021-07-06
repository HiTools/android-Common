package dev.hitools.noah.modules.init.splash.task

import android.text.TextUtils
import dev.hitools.noah.manager.UserManager
import dev.hitools.noah.modules.account.common.AppModel
import dev.hitools.noah.utils.http.okhttp.interceptor.AppHttpInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class UserTask : ITask {
    override fun startAsync(scope: CoroutineScope) = scope.async {
        val accessToken = UserManager.instance.getAccessToken()
        if (TextUtils.isEmpty(accessToken)) {
            status = Status.None
            return@async
        }
        AppHttpInterceptor.token = accessToken
        val result = AppModel.instance.loginByToken()

        status = if (result.isSuccess) {
            UserManager.instance.setUserContainer(result.data)
            Status.LoginSuccess
        } else {
            Status.LoginFailed
        }
    }


    companion object {
        internal var status: Status? = Status.None
    }

    enum class Status {
        /**
         * 无Token信息可直接进行下一步
         */
        None,

        /**
         * 登录成功
         */
        LoginSuccess,

        /**
         * 登录失败
         */
        LoginFailed,
    }
}