package com.return_result.notes.fragments

import android.animation.LayoutTransition
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.return_result.notes.R
import com.return_result.notes.activities.MainActivity
import com.return_result.notes.helpers.OperationsHelper
import com.return_result.notes.miscellaneous.Operation
import com.return_result.notes.xml.BaseNote

class Search : NotallyFragment() {

    private var textWatcher: TextWatcher? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.keyword = String()
        binding?.FrameLayout?.layoutTransition = LayoutTransition()

        textWatcher = object : TextWatcher {
            override fun afterTextChanged(query: Editable?) {
                model.keyword = query.toString().trim()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        (mContext as MainActivity).binding.EnterSearchKeyword.addTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (mContext as MainActivity).binding.EnterSearchKeyword.removeTextChangedListener(textWatcher)
    }


    override fun getObservable() = model.searchResults

    override fun getFragmentID() = R.id.SearchFragment

    override fun getBackground() = R.drawable.search

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