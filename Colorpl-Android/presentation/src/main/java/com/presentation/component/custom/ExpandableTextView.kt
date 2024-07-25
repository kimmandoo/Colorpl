package com.presentation.component.custom

import android.content.Context
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginLeft
import androidx.core.view.marginRight

class ExpandableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var originalText: String = ""
    private var trimmedText: String = ""
    private var trimmedLength: Int = 0
    private var isExpanded: Boolean = false
    private val expandText = " 더보기"

    init {
        maxLines = 2
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            trimText()
        }
    }

    private fun trimText() {
        originalText = text.toString()
        val availableWidth = width - marginLeft - marginRight
        val textPaint = TextPaint(paint)
        val staticLayout = StaticLayout.Builder.obtain(
            originalText,
            0,
            originalText.length,
            textPaint,
            availableWidth
        )
            .setMaxLines(maxLines)
            .build()

        if (staticLayout.lineCount > maxLines) {
            val lastLineStart = staticLayout.getLineStart(maxLines - 1)
            val lastLineEnd = staticLayout.getLineEnd(maxLines - 1)
            var lastLineText = originalText.substring(lastLineStart, lastLineEnd)

            val expandTextWidth = textPaint.measureText(expandText)
            var ellipsizedLength =
                textPaint.breakText(lastLineText, true, availableWidth - expandTextWidth, null)

            trimmedLength = lastLineStart + ellipsizedLength
            trimmedText = originalText.substring(0, trimmedLength - 1).trim() + "..." + expandText

            text = trimmedText
        } else {
            trimmedText = originalText
            text = originalText
        }
    }

    fun toggleExpand() {
        isExpanded = !isExpanded
        text = if (isExpanded) {
            maxLines = Integer.MAX_VALUE
            originalText
        } else {
            trimmedText
        }
    }

    fun isTextTrimmed(): Boolean = trimmedText != originalText
}