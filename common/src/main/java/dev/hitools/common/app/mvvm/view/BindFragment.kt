package dev.hitools.common.app.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.hitools.common.app.fragment.BaseFragment
import dev.hitools.common.app.mvvm.viewmodel.BaseViewModel
import dev.hitools.common.entries.status.Empty
import dev.hitools.common.entries.status.Error
import dev.hitools.common.entries.status.Loading
import dev.hitools.common.entries.status.Success
import dev.hitools.common.extensions.toast
import dev.hitools.common.utils.ReflectionUtils
import dev.hitools.common.utils.databinding.bus.Event
import dev.hitools.common.widget.StatusView
import java.lang.reflect.ParameterizedType

abstract class BindFragment<BINDING : ViewDataBinding, VM : BaseViewModel> : BaseFragment() {
    /**
     * Binding的实现类
     */
    protected lateinit var binding: BINDING

    /**
     * VM的实现类
     */
    protected var vm: VM? = null

    @Suppress("UNCHECKED_CAST")
    protected val viewModelClass: Class<VM> by lazy {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        type as Class<VM>
    }

    abstract fun getLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindContentView(container, getLayout())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        bindDataBindingValues()
        bindViewModel()
    }

    protected open fun bindContentView(container: ViewGroup?, layoutId: Int): View {
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    protected open fun initViews(view: View) {

    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun bindDataBindingValues() {
        ReflectionUtils.invokeMethod(binding, "setFragment", this, javaClass, false)
    }

    /**
     * ViewModel绑定
     */
    private fun bindViewModel() {
        if (!canBindViewModel()) {
            return
        }
        val vm = ViewModelProvider(this).get(viewModelClass)
        this.vm = vm
        initViewModel(vm)
        vm.init()
    }

    @Suppress("unused")
    protected open fun bindViewModel(cls: Class<VM>): VM {
        val vm = ViewModelProvider(this).get(cls)
        this.vm = vm
        initViewModel(vm)
        vm.init()
        return vm
    }

    protected open fun initViewModel(vm: VM) {
        val fragment = viewLifecycleOwner
        // dataBinding 设置vm参数
        ReflectionUtils.invokeMethod(binding, "setVm", vm, viewModelClass, false)
        lifecycle.addObserver(vm)

        vm.loadingStatus.observe(fragment, { changeLoadingStatus(it) })
        vm.errorStatus.observe(fragment, { changeErrorStatus(it) })
        vm.successStatus.observe(fragment, { changeSuccessStatus(it) })
        vm.emptyStatus.observe(fragment, { changeEmptyStatus(it) })
        vm.toastMessage.observe(fragment, { showToast(it) })
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.let { lifecycle.removeObserver(it) }
    }

    /**
     * 是否可以进行自动bindViewModel
     */
    protected open fun canBindViewModel(): Boolean {
        return viewModelClass != BaseViewModel::class.java
    }

    /**
     * 改变LoadingDialog的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeLoadingStatus(loading: Event<Loading>) {
        loading.value?.let {
            if (it.status == Loading.Status.Show) {
                showLoading(it)
            } else {
                dismissLoading(it)
            }
        }
    }

    /**
     * 改变Error的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeErrorStatus(event: Event<Error>) {
        event.value?.let { showError(it) }
    }

    /**
     * 改变Success的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeSuccessStatus(event: Event<Success>) {
        event.value?.let { showSuccess(it) }
    }

    /**
     * 改变Empty的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeEmptyStatus(event: Event<Empty>) {
        event.value?.let { showEmpty(it) }
    }

    private fun showToast(event: Event<String>) {
        event.value?.let { toast(it) }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {
        super.onStatusClick(v, which)
        vm?.retryRequest()
    }
}