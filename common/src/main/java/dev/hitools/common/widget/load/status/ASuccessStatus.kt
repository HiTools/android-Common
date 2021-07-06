package dev.hitools.common.widget.load.status

import dev.hitools.common.widget.load.Loader

/**
 * Created by yuhaiyang on 2020/9/15.
 * 成功的加载状态
 */
abstract class ASuccessStatus : ALoadStatus(Loader.Type.Success)