package dev.hitools.noah.utils.checker

import android.content.Context
import android.view.View
import dev.hitools.common.extensions.getInteger
import dev.hitools.common.utils.watcher.checker.ITextChecker
import dev.hitools.noah.R

class PasswordChecker(context: Context) : ITextChecker {
    private val min: Int = context.getInteger(R.integer.min_password)
    private val max: Int = context.getInteger(R.integer.max_password)
    override fun check(view: View, text: String?): Boolean {
        return text?.length in min..max
    }
}
