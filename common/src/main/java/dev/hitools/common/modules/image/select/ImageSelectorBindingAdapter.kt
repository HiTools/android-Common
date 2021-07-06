package dev.hitools.common.modules.image.select

import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import dev.hitools.common.R
import dev.hitools.common.entries.Image
import dev.hitools.common.extensions.findDrawable


object ImageSelectorBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["preview", "current"])
    fun setStatus(view: FrameLayout, preview: Image?, current: Image) {
        val context = view.context
        val isCurrent = preview == current
        if (isCurrent) {
            if (current.cancelSelected) {
                view.foreground = context.findDrawable(R.drawable.shape_preview_image_current_unselected)
            } else {
                view.foreground = context.findDrawable(R.drawable.shape_preview_image_current_selected)
            }
        } else {
            if (current.cancelSelected) {
                view.foreground = context.findDrawable(R.drawable.shape_preview_image_not_current_unselected)
            } else {
                view.foreground = context.findDrawable(R.drawable.shape_transparent)
            }
        }
    }
}
