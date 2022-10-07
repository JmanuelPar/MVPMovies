package com.diego.mvpretrosample.utils

import android.content.Context
import com.diego.mvpretrosample.R

sealed class UIText {
    object NoConnect : UIText()
    object UnknownError : UIText()
    data class MessageException(val errorMessage: String) : UIText()
}

fun Context.getMyUIText(uiText: UIText): String {
    return when (uiText) {
        is UIText.NoConnect -> getString(R.string.no_connect_message)
        is UIText.UnknownError -> getString(R.string.error_message_unknown_retry)
        is UIText.MessageException -> String.format(
            getString(R.string.error_message_retry),
            uiText.errorMessage
        )
    }
}