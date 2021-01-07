package com.return_result.notes.viewmodels

import android.app.Application
import android.graphics.Typeface
import android.text.Editable
import android.text.Spannable
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import com.return_result.notes.miscellaneous.applySpans
import com.return_result.notes.xml.BaseNote
import com.return_result.notes.xml.Note
import com.return_result.notes.xml.SpanRepresentation
import java.util.*
import kotlin.collections.HashSet

class TakeNoteModel(app: Application) : NotesModel(app) {

    var body = Editable.Factory.getInstance().newEditable(String())

    override fun getBaseNote(): BaseNote {
        return Note(title, file.path, labels.value ?: HashSet(), timestamp.toString(), body.toString().trimEnd(), body.getFilteredSpans())
    }

    override fun setStateFromBaseNote(baseNote: BaseNote) {
        baseNote as Note
        title = baseNote.title
        timestamp = baseNote.timestamp.toLong()
        body = baseNote.body.applySpans(baseNote.spans)
        labels.value = baseNote.labels
    }

    private fun Spannable.getFilteredSpans(): ArrayList<SpanRepresentation> {
        val representations = LinkedHashSet<SpanRepresentation>()
        val spans = getSpans(0, length, Any::class.java)
        spans.forEach { span ->
            val end = getSpanEnd(span)
            val start = getSpanStart(span)
            val representation = SpanRepresentation(false, false, false, false, false, start, end)

            when (span) {
                is StyleSpan -> {
                    representation.isBold = span.style == Typeface.BOLD
                    representation.isItalic = span.style == Typeface.ITALIC
                }
                is URLSpan -> representation.isLink = true
                is TypefaceSpan -> representation.isMonospace = span.family == "monospace"
                is StrikethroughSpan -> representation.isStrikethrough = true
            }

            if (representation.isNotUseless()) {
                representations.add(representation)
            }
        }
        return getFilteredRepresentations(ArrayList(representations))
    }

    private fun getFilteredRepresentations(representations: ArrayList<SpanRepresentation>): ArrayList<SpanRepresentation> {
        representations.forEachIndexed { index, representation ->
            val match = representations.find { spanRepresentation ->
                spanRepresentation.isEqualInSize(representation)
            }
            if (match != null && representations.indexOf(match) != index) {
                representation.isBold = match.isBold
                representation.isLink = match.isLink
                representation.isItalic = match.isItalic
                representation.isMonospace = match.isMonospace
                representation.isStrikethrough = match.isStrikethrough

                val copy = ArrayList(representations)
                copy[index] = representation
                copy.remove(match)
                return getFilteredRepresentations(copy)
            }
        }
        return representations
    }
}