package dev.hitools.common.entries.status

import androidx.annotation.StringRes
import dev.hitools.common.utils.StringUtils

class Empty {
    var message: String? = null
    var messageRes: Int = 0

    companion object {

        /**
         * New
         */
        fun new(message: String? = StringUtils.EMPTY): Empty {
            val empty = Empty()
            empty.message = message
            return empty
        }

        /**
         * New
         */
        fun new(@StringRes messageRes: Int): Empty {
            val empty = Empty()
            empty.messageRes = messageRes
            return empty
        }
    }
}
