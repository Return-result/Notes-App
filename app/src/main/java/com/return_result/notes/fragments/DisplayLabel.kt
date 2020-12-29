package com.return_result.notes.fragments

import androidx.lifecycle.MutableLiveData
import com.return_result.notes.R
import com.return_result.notes.helpers.OperationsHelper
import com.return_result.notes.miscellaneous.Constants
import com.return_result.notes.miscellaneous.Operation
import com.return_result.notes.xml.BaseNote

class DisplayLabel : NotallyFragment() {

    override fun getObservable(): MutableLiveData<ArrayList<BaseNote>>? {
        val label = arguments?.getString(Constants.argLabelKey)!!
        return model.getLabelledNotes(label)
    }

    override fun getFragmentID() = R.id.DisplayLabelFragment

    override fun getBackground() = R.drawable.label

    override fun getSupportedOperations(operationsHelper: OperationsHelper, baseNote: BaseNote): ArrayList<Operation> {
        return ArrayList()
    }
}