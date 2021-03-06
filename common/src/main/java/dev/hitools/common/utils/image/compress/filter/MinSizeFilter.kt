package dev.hitools.common.utils.image.compress.filter

import dev.hitools.common.utils.image.compress.ImageInfo
import dev.hitools.common.widget.PrintView

/**
 * Created by yuhaiyang on 2019-12-25.
 *
 */
class MinSizeFilter(val size: Long, val unit: Unit = Unit.KB) : ICompressFilter {
    override fun filter(info: ImageInfo): Boolean {
        // 因为某些时候byteCount 获取的可能为O
        if (info.byteCount == 0L) {
            return false
        }
        return info.byteCount <= size * unit.size
    }


    enum class Unit(val size: Long) {
        B(1),
        KB(1024),
        MB(1024 * 1024),
        GB(1024 * 1024 * 1024)
    }
}