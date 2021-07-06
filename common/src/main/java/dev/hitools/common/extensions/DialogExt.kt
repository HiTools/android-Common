package dev.hitools.common.extensions

import android.app.Dialog
import androidx.viewbinding.ViewBinding
import dev.hitools.common.extensions.binding.DialogBinding


/**
 * 方便获取ViewDataBinding
 */
inline fun <reified T : ViewBinding> Dialog.binding() = DialogBinding(T::class.java, context.inflater)