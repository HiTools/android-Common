package dev.hitools.common.utils.image.compress.adapter

import dev.hitools.common.utils.DateUtils
import dev.hitools.common.utils.image.compress.ImageInfo

/**
 * Created by yuhaiyang on 2019-12-25.
 *
 */
class RenameDateTimeAdapter : IRenameAdapter {
    override fun rename(info: ImageInfo): String {
        val dateTime = DateUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss_SSS")
        return "IMG_${dateTime}_${info.position}"
    }
}