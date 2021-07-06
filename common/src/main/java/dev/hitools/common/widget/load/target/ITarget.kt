package dev.hitools.common.widget.load.target

import dev.hitools.common.widget.load.LoadLayout
import dev.hitools.common.widget.load.Loader

/**
 * Created by yuhaiyang on 2020/9/15.
 * 目标对象
 */
interface ITarget {

    fun replace(loader: Loader): LoadLayout
}