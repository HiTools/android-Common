package dev.hitools.common.utils.watcher.checker

import android.view.View
import dev.hitools.common.extensions.isPhone

class PhoneNumberChecker : ITextChecker {
    override fun check(view: View, text: String?): Boolean {
        return text?.isPhone() == true
    }
}
