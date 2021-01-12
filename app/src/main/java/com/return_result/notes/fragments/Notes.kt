package com.return_result.notes.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import com.return_result.notes.R
import com.return_result.notes.activities.MainActivity
import com.return_result.notes.activities.MakeList
import com.return_result.notes.activities.TakeNote
import com.return_result.notes.helpers.MenuHelper
import com.return_result.notes.helpers.OperationsHelper
import com.return_result.notes.miscellaneous.Operation
import com.return_result.notes.xml.BaseNote

class Notes : NoteFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (mContext as MainActivity).binding.TakeNoteFAB.setOnClickListener {
            displayNoteTypes()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Search) {
            findNavController().navigate(R.id.NotesFragmentToSearchFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
    }


    private fun displayNoteTypes() {
        MenuHelper(mContext)
            .addItem(Operation(R.string.make_list, R.drawable.checkbox) { goToActivity(MakeList::class.java) })
            .addItem(Operation(R.string.take_note, R.drawable.edit) { goToActivity(TakeNote::class.java) })
            .show()
    }


    override fun getObservable() = model.notes

    override fun getFragmentID() = R.id.NotesFragment

    override fun getBackground() = R.drawable.notebook

    override fun getSupportedOperations(operationsHelper: OperationsHelper, baseNote: BaseNote): ArrayList<Operation> {
        val operations = ArrayList<Operation>()
        operations.add(Operation(R.string.share, R.drawable.share) { operationsHelper.shareNote(baseNote) })
        operations.add(Operation(R.string.labels, R.drawable.label) { labelBaseNote(baseNote) })
        operations.add(Operation(R.string.export, R.drawable.export) { showExportDialog(baseNote) })
        operations.add(Operation(R.string.delete, R.drawable.delete) { model.moveBaseNoteToDeleted(baseNote) })
        operations.add(Operation(R.string.archive, R.drawable.archive) { model.moveBaseNoteToArchive(baseNote) })
        return operations
    }
}