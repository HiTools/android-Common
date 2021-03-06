/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.common.app.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dev.hitools.common.R
import dev.hitools.common.app.mvp.IViewStatus
import dev.hitools.common.entries.status.Empty
import dev.hitools.common.entries.status.Error
import dev.hitools.common.entries.status.Loading
import dev.hitools.common.entries.status.Success
import dev.hitools.common.extensions.dialog
import dev.hitools.common.widget.StatusView
import dev.hitools.common.widget.TopBar
import dev.hitools.common.widget.loading.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment(), StatusView.OnStatusViewListener, IViewStatus, TopBar.OnTopBarListener {

    /**
     * Loading的Dialog
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var loadingDialog: LoadingDialog? = null

    /**
     * 状态的View
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var rootStatusView: StatusView? = null

    /**
     * 用来回收的Handler
     */
    protected var handler: Handler? = null

    protected val supportFragmentManager
        get() = activity?.supportFragmentManager

    val topBarHeight: Int by lazy {
        val theme = activity?.theme ?: return@lazy 0

        val typedValue = TypedValue()
        if (theme.resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        } else {
            0
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topBar: TopBar? = view.findViewById(R.id.topBar)
        topBar?.setOnTopBarListener(this)

        this.rootStatusView = attachStatusView()
        val statusView: View? = view.findViewById(R.id.statusView)
        if (statusView is StatusView) {
            this.rootStatusView = statusView
        }
        this.rootStatusView?.setOnStatusViewListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        detachStatusView()
    }
    //************************ 重写 各种事件区域*********************** //

    /**
     * TopBar的左侧点击事件
     */
    override fun onLeftClick(v: View) {}

    /**
     * TopBar的右侧点击事件
     */
    override fun onRightClick(v: View) {}

    /**
     * TopBar的中间Title点击事件
     */
    override fun onTitleClick(v: View) {}


    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }


    override fun onStatusClick(v: View, which: StatusView.Which) {
    }

    override fun showLoading(loading: Loading) {
        activity?.runOnUiThread {
            when (loading.type) {
                Loading.Type.Dialog -> {
                    loadingDialog = LoadingDialog.show(activity, loadingDialog, loading.tag)
                }
                Loading.Type.View -> {
                    rootStatusView?.showLoading(loading.tag)
                }
            }
        }
    }

    override fun dismissLoading(loading: Loading) {
        activity?.runOnUiThread {
            when (loading.type) {
                Loading.Type.Dialog -> {
                    LoadingDialog.dismiss(loadingDialog, loading.tag)
                }
                Loading.Type.View -> {
                    rootStatusView?.dismiss(loading.tag)
                }
            }
        }
    }

    override fun showError(error: Error) {
        activity?.runOnUiThread {
            when (error.showType) {
                Error.Type.Dialog -> {
                    if (error.message.isNullOrEmpty()) {
                        dialog(error.messageRes)
                    } else {
                        dialog(error.message)
                    }
                }
                Error.Type.View -> {
                    rootStatusView?.showError()
                }
            }
        }
    }

    override fun showSuccess(success: Success) {
    }
    override fun showEmpty(empty: Empty) {
        activity?.runOnUiThread { rootStatusView?.showEmpty() }
    }


    open fun attachStatusView(): StatusView? {
        if (!hasStatusView()) {
            return null
        }

        val viewParent = view?.parent
        return if (viewParent is ViewGroup) {
            StatusView.attach(viewParent, getStatusViewTopMargin())
        } else {
            null
        }
    }

    open fun detachStatusView() {
        if (!hasStatusView()) {
            return
        }
        val viewParent = view?.parent
        if (viewParent is ViewGroup) {
            StatusView.detach(viewParent)
        }
    }

    /**
     * 获取statusView的TopMargin
     */
    open fun getStatusViewTopMargin(): Int {
        return 0
    }

    /**
     * 获取statusView的TopMargin
     */
    open fun hasStatusView(): Boolean {
        return false
    }

    /**
     * 判断是否在主线程
     */
    fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    /**
     * 通过协程  在主线程上运行
     */
    fun mainThread(block: () -> Unit) = lifecycleScope.launch(Dispatchers.Main) {
        block()
    }
}
