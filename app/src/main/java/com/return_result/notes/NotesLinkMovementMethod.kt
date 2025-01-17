package com.return_result.notes

import android.graphics.RectF
import android.text.Selection
import android.text.Spannable
import android.text.method.ArrowKeyMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * Inspired by https://github.com/saket/Better-Link-Movement-Method
 * Intercepts touch events on links and dispatches them accordingly
 * (while updating the URL)
 */
class NotesLinkMovementMethod(private var onURLClick: (span: URLSpan) -> Unit) :
    ArrowKeyMovementMethod() {

    private val touchedLineBounds = RectF()
    private var isUrlHighlighted = false

    private var clickableSpanUnderTouchOnActionDown: ClickableSpan? = null

    override fun onTouchEvent(textView: TextView, text: Spannable, event: MotionEvent): Boolean {
        textView.autoLinkMask = 0

        val linkSpanUnderTouch = findLinkSpanUnderTouch(textView, text, event)

        if (event.action == MotionEvent.ACTION_DOWN) {
            clickableSpanUnderTouchOnActionDown = linkSpanUnderTouch
        }

        val touchStartedOverALinkSpan = clickableSpanUnderTouchOnActionDown != null

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                linkSpanUnderTouch?.let { highlightUrl(it, text) }
                touchStartedOverALinkSpan
            }
            MotionEvent.ACTION_UP -> {
                if (touchStartedOverALinkSpan && linkSpanUnderTouch === clickableSpanUnderTouchOnActionDown) {
                    dispatchUrlClick(linkSpanUnderTouch)
                }
                cleanupOnTouchUp(textView)
                touchStartedOverALinkSpan
            }
            MotionEvent.ACTION_CANCEL -> {
                cleanupOnTouchUp(textView)
                false
            }
            MotionEvent.ACTION_MOVE -> {
                if (linkSpanUnderTouch != null) {
                    highlightUrl(linkSpanUnderTouch, text)
                } else removeUrlHighlightColor(textView)
                touchStartedOverALinkSpan
            }
            else -> false
        }
    }


    private fun cleanupOnTouchUp(textView: TextView) {
        clickableSpanUnderTouchOnActionDown = null
        removeUrlHighlightColor(textView)
    }

    private fun findLinkSpanUnderTouch(textView: TextView, text: Spannable, event: MotionEvent): URLSpan? {
        var touchX = event.x.toInt()
        var touchY = event.y.toInt()

        touchX -= textView.totalPaddingLeft
        touchY -= textView.totalPaddingTop

        touchX += textView.scrollX
        touchY += textView.scrollY

        val layout = textView.layout
        val touchedLine = layout.getLineForVertical(touchY)
        val touchOffset = layout.getOffsetForHorizontal(touchedLine, touchX.toFloat())

        touchedLineBounds.left = layout.getLineLeft(touchedLine)
        touchedLineBounds.top = layout.getLineTop(touchedLine).toFloat()
        touchedLineBounds.right = layout.getLineWidth(touchedLine) + touchedLineBounds.left
        touchedLineBounds.bottom = layout.getLineBottom(touchedLine).toFloat()

        return if (touchedLineBounds.contains(touchX.toFloat(), touchY.toFloat())) {
            val spans = text.getSpans(touchOffset, touchOffset, URLSpan::class.java)
            return spans.firstOrNull()
        } else null
    }


    private fun removeUrlHighlightColor(textView: TextView) {
        if (!isUrlHighlighted) {
            return
        }
        isUrlHighlighted = false
        Selection.removeSelection(textView.text as Spannable)
    }

    private fun highlightUrl(span: URLSpan?, text: Spannable) {
        if (isUrlHighlighted) {
            return
        }
        isUrlHighlighted = true
        val spanStart = text.getSpanStart(span)
        val spanEnd = text.getSpanEnd(span)
        Selection.setSelection(text, spanStart, spanEnd)
    }

    private fun dispatchUrlClick(urlSpan: URLSpan?) {
        urlSpan?.let {
            onURLClick.invoke(it)
        }
    }
}