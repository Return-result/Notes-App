package com.return_result.notes.fragments

import com.return_result.notes.R
import com.return_result.notes.helpers.OperationsHelper
import com.return_result.notes.miscellaneous.Operation
import com.return_result.notes.xml.BaseNote

class Archived : NotallyFragment() {

    override fun getObservable() = model.archivedNotes

    override fun getFragmentID() = R.id.ArchivedFragment

    override fun getBackground() = R.drawable.archive

    override fun getSupportedOperations(operationsHelper: OperationsHelper, baseNote: BaseNote): ArrayList<Operation> {
        val operations = ArrayList<Operation>()
        operations.add(Operation(R.string.unarchive, R.drawable.unarchive) { model.restoreBaseNote(baseNote) })
        return operations
    }
}