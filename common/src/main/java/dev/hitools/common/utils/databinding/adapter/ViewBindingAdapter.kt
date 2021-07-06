package dev.hitools.common.utils.databinding.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import dev.hitools.common.extensions.dp2px
import dev.hitools.common.extensions.setPaddingHorizontal
import dev.hitools.common.extensions.setPaddingVertical

/**
 * View的Binding适配器
 */
object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("paddingHorizontal")
    fun setPaddingHorizontal(view: View, value: Int) {
        view.setPaddingHorizontal(value.dp2px())
    }

    @JvmStatic
    @BindingAdapter("paddingVertical")
    fun setPaddingVertical(view: View, value: Int) {
        view.setPaddingVertical(value.dp2px())
    }
}