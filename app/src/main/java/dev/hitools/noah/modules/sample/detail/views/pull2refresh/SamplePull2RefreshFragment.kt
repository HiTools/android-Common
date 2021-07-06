package dev.hitools.noah.modules.sample.detail.views.pull2refresh

import android.os.Bundle
import android.util.Log
import android.view.View
import dev.hitools.common.adapter.BindAdapter
import dev.hitools.common.extensions.toJSON
import dev.hitools.common.widget.pulltorefresh.headers.google.GoogleStyleHeader
import dev.hitools.common.widget.pulltorefresh.recycleview.LoadMoreAdapter
import dev.hitools.noah.BR
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FSamplePull2refreshBinding
import dev.hitools.noah.modules.base.mvvm.view.Pull2RefreshFragment
import dev.hitools.noah.modules.sample.entries.SampleTestPage

/**
 * Created by yuhaiyang on 2019-09-19.
 *
 */
class SamplePull2RefreshFragment : Pull2RefreshFragment<FSamplePull2refreshBinding, SamplePull2RefreshViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_pull2refresh
    val adapter = BindAdapter<SampleTestPage>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context

        adapter.addLayout(BR.item, R.layout.i_sample_pull2refresh)
        val footer = LoadMoreAdapter(adapter)
        binding.list.adapter = footer

        binding.pull2refresh.setHeader(GoogleStyleHeader(context))
        binding.pull2refresh.setFooter(footer)
    }

    override fun initViewModel(vm: SamplePull2RefreshViewModel) {
        super.initViewModel(vm)
        vm.data.observe(this, {
            Log.i("yhy", "list = " + it?.toJSON())
            adapter.data = it
        })
    }

    override fun onLoadData(v: View, pager: Int, refresh: Boolean) {
        binding.vm?.getData(pager)
    }

    override fun hasStatusView(): Boolean {
        return false
    }
}