package dev.hitools.common.utils.databinding.adapter

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import dev.hitools.common.extensions.loadUrl
import dev.hitools.common.widget.textview.TextViewPro

object TextViewProBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["image2Url", "image2Options", "image2Placeholder"], requireAll = false)
    fun rightImage2Url(
            view: TextViewPro,
            url: String?,
            options: RequestOptions = RequestOptions.centerCropTransform(),
            placeholder: Drawable
    ) {
        view.rightImageView2?.loadUrl(url, options, placeholder)
    }
}