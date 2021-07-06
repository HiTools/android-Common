package dev.hitools.noah.modules.account.modify

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.hitools.common.extensions.mainThread
import dev.hitools.noah.manager.UserManager
import dev.hitools.noah.modules.account.common.AppModel
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.launch

class ModifyUserViewModel(application: Application) : AppBaseViewModel(application) {
    private val _avatarPath = MutableLiveData<String>()
    val avatarPath: LiveData<String>
        get() = _avatarPath


    override fun init() {
        _avatarPath.value = UserManager.instance.getAvatar()
    }

    fun uploadAvatar(path: String) = viewModelScope.launch {
        val accountModel = AppModel.instance
        val result = request { accountModel.uploadAvatar(path) }
        result?.let {
            UserManager.setAvatar(it)
            mainThread { _avatarPath.value = it }
        }
    }
}