package com.return_result.notes.recyclerview.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.return_result.notes.databinding.RecyclerViewItemBinding
import com.return_result.notes.helpers.SettingsHelper
import com.return_result.notes.recyclerview.viewholders.BaseNoteViewHolder
import com.return_result.notes.xml.BaseNote

class BaseNoteAdapter(private val context: Context) :
    ListAdapter<BaseNote, BaseNoteViewHolder>(DiffCallback()) {

    var onNoteClicked: ((position: Int) -> Unit)? = null
    var onNoteLongClicked: ((position: Int) -> Unit)? = null

    private val settingsHelper = SettingsHelper(context)

    override fun onBindViewHolder(holder: BaseNoteViewHolder, position: Int) {
        val baseNote = getItem(position)
        holder.bind(baseNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseNoteViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return BaseNoteViewHolder(binding, settingsHelper, onNoteClicked, onNoteLongClicked)
    }


    class DiffCallback : DiffUtil.ItemCallback<BaseNote>() {
        override fun areItemsTheSame(oldItem: BaseNote, newItem: BaseNote): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(oldItem: BaseNote, newItem: BaseNote): Boolean {
            return oldItem == newItem
        }
    }
}