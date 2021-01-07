package com.return_result.notes

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

/**
 * Custom implementation that fixes a bug in Lollipop where clicking on the overflow icon
 * in the custom text selection mode causes the mode to end.
 * For more information, see this -> https://issuetracker.google.com/issues/36937508
 */
class NotesEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextInputEditText(context, attrs, defStyleAttr) {

    var isActionModeOn = false

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (!isActionModeOn) {
            super.onWindowFocusChanged(hasWindowFocus)
        }
    }
}