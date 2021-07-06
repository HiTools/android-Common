package dev.hitools.common.utils.watcher.checker

import android.view.View
import dev.hitools.common.utils.RegexValidateUtils

class IdCardChecker : ITextChecker {
    override fun check(view: View, text: String?): Boolean {
        return RegexValidateUtils.checkIdCard(text)
    }
}
