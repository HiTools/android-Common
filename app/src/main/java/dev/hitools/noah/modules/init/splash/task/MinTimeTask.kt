package dev.hitools.noah.modules.init.splash.task

import dev.hitools.noah.constant.Configure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class MinTimeTask : ITask {

    override fun startAsync(scope: CoroutineScope) = scope.async {
        delay(Configure.SPLASH_TIME)
    }
}