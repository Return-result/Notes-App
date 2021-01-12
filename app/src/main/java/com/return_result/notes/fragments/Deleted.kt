package com.return_result.notes.fragments

import com.return_result.notes.R
import com.return_result.notes.helpers.OperationsHelper
import com.return_result.notes.miscellaneous.Operation
import com.return_result.notes.xml.BaseNote

class Deleted : NoteFragment() {

    override fun getObservable() = model.deletedNotes

    override fun getFragmentID() = R.id.DeletedFragment

    override fun getBackground() = R.drawable.delete

    override fun getSupportedOperations(operationsHelper: OperationsHelper, baseNote: BaseNote): ArrayList<Operation> {
        val operations = ArrayList<Operation>()
        operations.add(Operation(R.string.restore, R.drawable.restore) { model.restoreBaseNote(baseNote) })
        operations.add(Operation(R.string.delete_forever, R.drawable.delete) { confirmDeletion(baseNote) })
        return operations
    }
}