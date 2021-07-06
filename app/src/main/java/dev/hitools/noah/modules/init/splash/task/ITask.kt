package dev.hitools.noah.modules.init.splash.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface ITask {
    /**
     * 开始
     */
    fun startAsync(scope: CoroutineScope): Deferred<*>
}