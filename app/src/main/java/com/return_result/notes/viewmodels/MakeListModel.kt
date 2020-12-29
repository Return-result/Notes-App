package com.return_result.notes.viewmodels

import android.app.Application
import com.return_result.notes.xml.BaseNote
import com.return_result.notes.xml.List
import com.return_result.notes.xml.ListItem

class MakeListModel(app: Application) : NotallyModel(app) {

    val items = ArrayList<ListItem>()

    override fun getBaseNote(): BaseNote {
        val listItems = items.filter { it.body.isNotBlank() }
        return List(title, file.path, labels.value ?: HashSet(), timestamp.toString(), listItems)
    }

    override fun setStateFromBaseNote(baseNote: BaseNote) {
        baseNote as List
        title = baseNote.title
        timestamp = baseNote.timestamp.toLong()

        items.clear()
        items.addAll(baseNote.items)

        labels.value = baseNote.labels
    }
}