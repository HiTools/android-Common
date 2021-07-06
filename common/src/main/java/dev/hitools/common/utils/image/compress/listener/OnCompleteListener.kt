package dev.hitools.common.utils.image.compress.listener

import dev.hitools.common.utils.image.compress.CompressResult

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */
interface OnCompleteListener {

    /**
     * 最终结果
     * 如果存在部分失败，则相关File为null
     */
    fun onResult(result: CompressResult)
}