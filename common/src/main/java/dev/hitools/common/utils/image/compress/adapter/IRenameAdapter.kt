package dev.hitools.common.utils.image.compress.adapter

import dev.hitools.common.utils.image.compress.ImageInfo

/**
 * Created by yuhaiyang on 2019-12-25.
 * 重命名的Adapter
 */
interface IRenameAdapter {
    fun rename(info: ImageInfo): String
}