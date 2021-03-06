package dev.hitools.noah.modules.main.tab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class Tab3ViewModel (app: Application) : AppBaseViewModel(app) {

    private val _test = MutableLiveData<String>()
    val test: LiveData<String>
        get() = _test

}