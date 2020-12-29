package com.return_result.notes.recyclerview.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.return_result.notes.databinding.LabelItemBinding

class LabelsViewHolder(private val binding: LabelItemBinding,
                       onLabelClicked: ((position: Int) -> Unit)?,
                       onLabelLongClicked: ((position: Int) -> Unit)?) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onLabelClicked?.invoke(adapterPosition)
        }

        binding.root.setOnLongClickListener {
            onLabelLongClicked?.invoke(adapterPosition)
            return@setOnLongClickListener true
        }
    }

    fun bind(label: String) {
        binding.DisplayLabel.text = label
    }
}