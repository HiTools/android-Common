package dev.hitools.noah.modules.base.mvvm.view

import android.app.Dialog
import android.view.View
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import dev.hitools.common.app.mvvm.view.BindActivity
import dev.hitools.common.extensions.fullWindow
import dev.hitools.common.extensions.isLightStatusBars
import dev.hitools.common.widget.watermark.WaterMarkView
import dev.hitools.noah.App
import dev.hitools.noah.manager.VersionManager
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import dev.hitools.noah.ui.widget.dialog.VersionDialog


/**
 * Created by yuhaiyang on 2018/8/8.
 * App层面的BaseActivity
 */
abstract class AppBindActivity<T : ViewDataBinding, VM : AppBaseViewModel> : BindActivity<T, VM>() {

    /**
     * 检测升级的Dialog
     */
    private var versionDialog: Dialog? = null

    /**
     * 获取应用的Application
     */
    @Suppress("unused")
    protected val app: App
        get() = application as App


    override fun initViews() {
        super.initViews()
        WaterMarkView.attachToActivity(this)
    }

    override fun onResume() {
        super.onResume()

        if (needShowUpdateVersionDialog() && VersionManager.instance.hasNewVersion()) {
            showVersionDialog()
        }
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        dismissVersionDialog()
    }

    /**
     * 是否需要显示升级dialog
     */
    protected open fun needShowUpdateVersionDialog(): Boolean {
        return true
    }

    override fun resetStatusBar() {
        super.resetStatusBar()
        fullWindow()
    }

    /**
     * 显示升级Dialog
     */
    private fun showVersionDialog() {
        if (versionDialog == null) {
            versionDialog = VersionDialog(this)
        }

        if (!versionDialog!!.isShowing) {
            versionDialog!!.show()
        }
    }

    /**
     * 隐藏升级的Dialog
     */
    private fun dismissVersionDialog() {
        if (versionDialog != null && versionDialog!!.isShowing) {
            versionDialog!!.dismiss()
        }
        versionDialog = null
    }

}
