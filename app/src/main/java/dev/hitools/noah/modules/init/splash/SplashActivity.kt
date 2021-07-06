package dev.hitools.noah.modules.init.splash

import android.os.Bundle
import androidx.lifecycle.Observer
import dev.hitools.common.entries.status.Success
import dev.hitools.common.extensions.fullWindow
import dev.hitools.common.utils.router.AppRouter
import dev.hitools.noah.R
import dev.hitools.noah.databinding.ActivitySpalshBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindActivity
import dev.hitools.noah.modules.main.index.MainActivity

/**
 * Created by yuhaiyang on 2018/3/27.
 * Splash
 */
class SplashActivity : AppBindActivity<ActivitySpalshBinding, SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullWindow()
        bindContentView(R.layout.activity_spalsh)
    }

    override fun initViewModel(vm: SplashViewModel) {
        super.initViewModel(vm)
        // 注册状态
        vm.permissionStatus.observe(activity, Observer { onPermission()})
        // 初始化
        vm.preInit(this@SplashActivity)
    }


    private fun onPermission() {
        binding.vm?.start()
    }

    override fun showSuccess(success: Success) {
        super.showSuccess(success)
        AppRouter.with(context)
            .target(MainActivity::class.java)
            .finishSelf()
            .start()
    }

    /**
     * 是否显示版本更新的Dialog
     */
    override fun needShowUpdateVersionDialog(): Boolean {
        return false
    }
}
