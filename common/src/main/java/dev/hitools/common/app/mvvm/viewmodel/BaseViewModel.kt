package dev.hitools.common.app.mvvm.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import dev.hitools.common.entries.status.Empty
import dev.hitools.common.entries.status.Error
import dev.hitools.common.entries.status.Loading
import dev.hitools.common.entries.status.Success
import dev.hitools.common.extensions.isMainThread
import dev.hitools.common.extensions.mainThread
import dev.hitools.common.utils.StringUtils
import dev.hitools.common.utils.databinding.bus.Event
import kotlinx.coroutines.cancel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), LifecycleEventObserver {

    /**
     * 注意这里用的是ApplicationContext
     */
    protected val context: Context = application.applicationContext

    /**
     * Loading的状态
     */
    private val _loadingStatus = MutableLiveData<Event<Loading>>()
    val loadingStatus: LiveData<Event<Loading>>
        get() = _loadingStatus

    /**
     * 错误的状态
     */
    private val _errorStatus = MutableLiveData<Event<Error>>()
    val errorStatus: LiveData<Event<Error>>
        get() = _errorStatus

    /**
     * 成功的状态
     */
    private val _successStatus = MutableLiveData<Event<Success>>()
    val successStatus: LiveData<Event<Success>>
        get() = _successStatus

    /**
     * 空数据状态
     */
    private val _emptyStatus = MutableLiveData<Event<Empty>>()
    val emptyStatus: LiveData<Event<Empty>>
        get() = _emptyStatus

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage

    /**
     * 初始化
     */
    open fun init() {}

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            else -> Log.d(TAG, "on other lifecycle event $event")

        }
    }

    protected open fun onCreate() {
    }

    protected open fun onResume() {
    }

    protected open fun onStart() {
    }

    protected open fun onPause() {
    }

    protected open fun onStop() {
    }

    protected open fun onDestroy() {
        viewModelScope.cancel()
    }

    /**
     * 重试
     */
    open fun retryRequest() {}

    /**
     * 显示加载动画
     */
    fun showLoading() {
        showLoading(Loading.dialog())
    }

    fun showLoading(loading: Loading) {
        if (isMainThread()) {
            _loadingStatus.value = Event(loading)
        } else {
            mainThread { _loadingStatus.value = Event(loading) }
        }
    }

    /**
     * 隐藏加载动画
     */
    fun dismissLoading() {
        dismissLoading(Loading.dismiss())
    }

    fun dismissLoading(loading: Loading) {
        loading.status = Loading.Status.Dismiss
        if (isMainThread()) {
            _loadingStatus.value = Event(loading)
        } else {
            mainThread { _loadingStatus.value = Event(loading) }
        }
    }

    /**
     * 展示错误信息
     */
    fun showError(message: String? = StringUtils.EMPTY) {
        showError(Error.toast(message))
    }

    fun showError(@StringRes messageRes: Int) {
        showError(Error.toast(messageRes))
    }

    fun showError(error: Error) {
        if (isMainThread()) {
            _errorStatus.value = Event(error)
        } else {
            mainThread { _errorStatus.value = Event(error) }
        }
    }

    /**
     * 展示成功的信息
     */
    fun showSuccess(code: Int = 0, message: String? = StringUtils.EMPTY) {
        showSuccess(Success.new(code, message))
    }

    fun showSuccess(@StringRes messageRes: Int) {
        showSuccess(Success.new(messageRes = messageRes))
    }

    fun showSuccess(success: Success) {
        if (isMainThread()) {
            _successStatus.value = Event(success)
        } else {
            mainThread { _successStatus.value = Event(success) }
        }
    }

    /**
     * 展示成功的信息
     */
    fun showEmpty(message: String? = StringUtils.EMPTY) {
        showEmpty(Empty.new(message))
    }

    fun showEmpty(@StringRes messageRes: Int) {
        showEmpty(Empty.new(messageRes))
    }

    fun showEmpty(empty: Empty) {
        if (isMainThread()) {
            _emptyStatus.value = Event(empty)
        } else {
            mainThread { _emptyStatus.value = Event(empty) }
        }
    }

    fun toast(message: String?) {
        if (message.isNullOrEmpty()) {
            return
        }
        if (isMainThread()) {
            _toastMessage.value = Event(message)
        } else {
            mainThread { _toastMessage.value = Event(message) }
        }
    }

    fun toast(@StringRes message: Int) {
        toast(context.getString(message))
    }

    companion object {
        private const val TAG = "BaseViewModel"
    }

}