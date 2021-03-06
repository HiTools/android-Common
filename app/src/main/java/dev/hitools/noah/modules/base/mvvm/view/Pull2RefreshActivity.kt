package dev.hitools.noah.modules.base.mvvm.view

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import dev.hitools.common.utils.databinding.bus.Event
import dev.hitools.common.widget.pulltorefresh.OnPullToRefreshListener
import dev.hitools.common.widget.pulltorefresh.PullToRefreshView
import dev.hitools.common.widget.pulltorefresh.headers.google.GoogleStyleHeader
import dev.hitools.noah.R
import dev.hitools.noah.modules.base.mvvm.viewmodel.Pull2RefreshViewModel
import dev.hitools.noah.modules.base.mvvm.viewmodel.Pull2RefreshViewModel.Pull2RefreshStatus

/**
 * Created by yuhaiyang on 2019-09-15.
 * 上拉加载更多下拉刷新的View
 */
abstract class Pull2RefreshActivity<T : ViewDataBinding, VM : Pull2RefreshViewModel> : AppBindActivity<T, VM>(), OnPullToRefreshListener {

    private var pull2refresh: PullToRefreshView? = null
    private var pager: Int = 1

    override fun initViews() {
        super.initViews()
        pull2refresh = findViewById(R.id.pull2refresh)
        pull2refresh?.setHeader(GoogleStyleHeader(this))
        pull2refresh?.setOnPullToRefreshListener(this)
    }

    override fun initViewModel(vm: VM) {
        super.initViewModel(vm)
        vm.pull2refreshStatus.observe(this, Observer { onPull2RefreshStatusChanged(it) })
    }

    override fun onRefresh(view: PullToRefreshView) {
        onLoadData(view, 1, true)
    }

    override fun onLoadMore(view: PullToRefreshView) {
        onLoadData(view, pager + 1, false)
    }

    open fun onPull2RefreshStatusChanged(event: Event<Pull2RefreshStatus>) {
        event.value?.let {
            when (it) {
                Pull2RefreshStatus.RefreshSuccess -> {
                    pull2refresh?.setRefreshSuccess()
                    pager = 1
                }
                Pull2RefreshStatus.RefreshFailed -> {
                    pull2refresh?.setRefreshFailed()
                }
                Pull2RefreshStatus.LoadMoreNormal -> {
                    pull2refresh?.setLoadMoreNormal()
                }
                Pull2RefreshStatus.LoadMoreSuccess -> {
                    pull2refresh?.setLoadMoreSuccess()
                    pager += 1
                }
                Pull2RefreshStatus.LoadMoreFailed -> {
                    pull2refresh?.setLoadMoreFailed()
                }
                Pull2RefreshStatus.LoadMoreEnd -> {
                    pull2refresh?.setLoadMoreEnd()
                }
            }
        }

    }

    protected open fun onLoadData(v: View, pager: Int, refresh: Boolean) {
        vm?.onLoadData(v, pager, refresh)
    }
}