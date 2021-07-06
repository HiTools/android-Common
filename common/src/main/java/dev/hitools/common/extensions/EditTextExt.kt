package dev.hitools.common.extensions

import android.widget.EditText
import dev.hitools.common.utils.StringUtils

fun EditText.trimText(): String {
    return this.text?.toString()?.trim { it <= ' ' } ?: StringUtils.EMPTY
}