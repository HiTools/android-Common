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

package dev.hitools.common.app.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import dev.hitools.common.R
import dev.hitools.common.app.mvp.IViewStatus
import dev.hitools.common.entries.status.Empty
import dev.hitools.common.entries.status.Error
import dev.hitools.common.entries.status.Loading
import dev.hitools.common.entries.status.Success
import dev.hitools.common.extensions.dialog
import dev.hitools.common.extensions.toast
import dev.hitools.common.utils.DeviceUtils
import dev.hitools.common.widget.StatusView
import dev.hitools.common.widget.TopBar
import dev.hitools.common.widget.loading.LoadingDialog


abstract class BaseActivity : AppCompatActivity(), StatusView.OnStatusViewListener,
    IViewStatus,
    TopBar.OnTopBarListener {
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

    protected var isActivityPaused: Boolean = false

    /**
     * Activity
     * isResumed 已经被占用..
     */
    var isActivityResumed: Boolean = false

    val context: Context
        get() = this

    val activity: BaseActivity
        get() = this

    val statusBarHeight: Int by lazy {
        DeviceUtils.getStatusBarHeight(activity)
    }

    val topBarHeight: Int by lazy {
        val typedValue = TypedValue()
        if (theme.resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        } else {
            0
        }
    }

    //************************ 生命周期 区域*********************** //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetStatusBar()
    }


    override fun onResume() {
        super.onResume()
        isActivityResumed = true
        isActivityPaused = false
    }

    override fun onPause() {
        super.onPause()
        isActivityResumed = false
        isActivityPaused = true
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清除Handler预防内存泄露
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    //************************ 初始化 区域*********************** //
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        attachStatusView()
        initNecessaryData()
        initViews()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        attachStatusView()
        initNecessaryData()
        initViews()
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        attachStatusView()
        initNecessaryData()
        initViews()
    }

    /**
     * 有一些数据要在initViews之前处理的在这个方法中处理
     */
    protected open fun initNecessaryData() {}

    protected open fun initViews() {
        // 主动设置TopBar
        val topBarView: View? = findViewById(R.id.topBar)
        if (topBarView is TopBar) {
            topBarView.setOnTopBarListener(this)
        }

        // 主动设置statusView
        val statusView: View? = findViewById(R.id.statusView)
        if (statusView is StatusView) {
            this.rootStatusView = statusView
            this.rootStatusView?.setOnStatusViewListener(this)
            this.rootStatusView?.dismiss()
            initStatusView()
        }
    }

    /**
     * TopBar的左侧点击事件
     */
    override fun onLeftClick(v: View) {
        onBackPressed()
    }

    /**
     * TopBar的右侧点击事件
     */
    override fun onRightClick(v: View) {

    }

    /**
     * TopBar的中间Title点击事件
     */
    override fun onTitleClick(v: View) {

    }

    /**
     * 用来设置全屏样式
     */
    protected open fun resetStatusBar() {}


    override fun showLoading(loading: Loading) {
        runOnUiThread {
            when (loading.type) {
                Loading.Type.Dialog -> {
                    loadingDialog = LoadingDialog.show(context, loadingDialog, loading.tag)
                }
                Loading.Type.View -> {
                    rootStatusView?.showLoading(loading.tag)
                }
            }
        }
    }


    override fun dismissLoading(loading: Loading) {
        runOnUiThread {
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
        runOnUiThread {
            when (error.showType) {
                Error.Type.Dialog -> {
                    if (error.message.isNullOrEmpty()) {
                        dialog(error.messageRes)
                    } else {
                        dialog(error.message!!)
                    }
                }
                Error.Type.Toast -> {
                    if (error.message.isNullOrEmpty()) {
                        toast(error.messageRes)
                    } else {
                        toast(error.message!!)
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
        runOnUiThread { rootStatusView?.showEmpty() }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {

    }

    protected open fun initStatusView() {}

    open fun attachStatusView() {
        if (!hasStatusView()) {
            return
        }
        StatusView.attachToActivity(activity, getStatusViewTopMargin())
    }

    /**
     * 获取statusView的TopMargin
     */
    open fun getStatusViewTopMargin(): Int {
        return statusBarHeight + topBarHeight
    }

    /**
     * 获取statusView的TopMargin
     */
    open fun hasStatusView(): Boolean {
        return false
    }

    companion object {
        /**
         * Activity的TYPE
         */
        const val KEY_TYPE = "activity_type"
    }
}

