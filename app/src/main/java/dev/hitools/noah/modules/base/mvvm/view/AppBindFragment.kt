package dev.hitools.noah.modules.base.mvvm.view

import androidx.databinding.ViewDataBinding
import dev.hitools.common.app.mvvm.view.BindFragment
import dev.hitools.common.utils.AppUtils
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel


abstract class AppBindFragment<T : ViewDataBinding, VM : AppBaseViewModel> : BindFragment<T, VM>() {
    override fun canBindViewModel(): Boolean {
        return viewModelClass != AppBaseViewModel::class.java
    }


    open fun onViewClick() {
        if (AppUtils.isFastDoubleClick) return
    }
}

