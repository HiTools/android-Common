package dev.hitools.noah

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import dev.hitools.common.utils.router.AppRouter
import dev.hitools.noah.utils.AppLifeCycleChecker
import dev.hitools.noah.utils.router.AppRouterConfigure

/**
 * Application
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        AppRouter.setConfigure(AppRouterConfigure())
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeCycleChecker())
    }

    companion object {
        private lateinit var sInstance: App
        val app
            get() = sInstance
    }
}
