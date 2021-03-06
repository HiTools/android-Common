package dev.hitools.noah.modules.sample.detail.views.pull2refresh

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.hitools.noah.modules.base.mvvm.viewmodel.Pull2RefreshViewModel
import dev.hitools.noah.modules.sample.entries.SampleTestPage
import dev.hitools.noah.modules.sample.main.SampleModel
import kotlinx.coroutines.launch

/**
 * Created by yuhaiyang on 2019-09-19.
 *
 */
class SamplePull2RefreshViewModel(app: Application) : Pull2RefreshViewModel(app) {
    private val sampleModel: SampleModel = SampleModel()
    private val _data = MutableLiveData<MutableList<SampleTestPage>>()
    val data: LiveData<MutableList<SampleTestPage>>
        get() = _data
    init {
        getData(loading = true)
    }

    fun getData(page: Int = 1, loading: Boolean = false) = viewModelScope.launch {
        pull2refresh(page, _data, loading) { sampleModel.testPage(page) }
    }

    override fun retryRequest() {
        super.retryRequest()
        getData(loading = true)
    }
}