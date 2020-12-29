package com.return_result.notes.recyclerview.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.return_result.notes.databinding.ListItemBinding
import com.return_result.notes.recyclerview.ListItemListener
import com.return_result.notes.recyclerview.viewholders.MakeListViewHolder
import com.return_result.notes.xml.ListItem
import java.util.*

class MakeListAdapter(private val context: Context, var items: ArrayList<ListItem>) :
    RecyclerView.Adapter<MakeListViewHolder>() {

    var listItemListener: ListItemListener? = null

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MakeListViewHolder, position: Int) {
        val listItem = items[position]
        holder.bind(listItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MakeListViewHolder(binding, listItemListener)
    }
}