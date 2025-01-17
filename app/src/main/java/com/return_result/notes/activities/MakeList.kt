package com.return_result.notes.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.button.MaterialButton
import com.return_result.notes.R
import com.return_result.notes.databinding.ActivityMakeListBinding
import com.return_result.notes.miscellaneous.getLocale
import com.return_result.notes.miscellaneous.setOnNextAction
import com.return_result.notes.recyclerview.ListItemListener
import com.return_result.notes.recyclerview.adapters.MakeListAdapter
import com.return_result.notes.recyclerview.viewholders.MakeListViewHolder
import com.return_result.notes.viewmodels.MakeListModel
import com.return_result.notes.xml.ListItem
import java.text.SimpleDateFormat
import java.util.*

class MakeList : NotesActivity() {

    private lateinit var adapter: MakeListAdapter
    private lateinit var binding: ActivityMakeListBinding
    override val model: MakeListModel by viewModels()

    lateinit var mAdViewMakeList : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.EnterTitle.setOnNextAction {
            moveToNext(-1)
        }

        setupListeners()
        setupRecyclerView()
        setupToolbar(binding.Toolbar)


        MobileAds.initialize(this) {}

        mAdViewMakeList = findViewById(R.id.adViewMakeList)
        val adRequest = AdRequest.Builder().build()
        mAdViewMakeList.loadAd(adRequest)

        if (model.isNewNote) {
            binding.EnterTitle.requestFocus()
            if (model.items.isEmpty()) {
                addListItem()
            }
        }

        binding.AddItem.setOnClickListener {
            addListItem()
        }

        setStateFromModel()
    }


    override fun shareNote() = operationsHelper.shareNote(model.title, model.items)


    private fun setupListeners() {
        binding.EnterTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                model.title = text.toString().trim()
            }
        })

        model.labels.observe(this, {
            binding.LabelGroup.removeAllViews()
            it?.forEach { label ->
                val displayLabel = View.inflate(this, R.layout.label, null) as MaterialButton
                displayLabel.text = label
                binding.LabelGroup.addView(displayLabel)
            }
        })
    }

    private fun setStateFromModel() {
        binding.EnterTitle.setText(model.title)
        val formatter = SimpleDateFormat(DateFormat, getLocale())
        binding.DateCreated.text = formatter.format(model.timestamp)
        adapter.notifyDataSetChanged()
    }


    private fun addListItem(position: Int = adapter.items.size) {
        val listItem = ListItem(String(), false)
        adapter.items.add(position, listItem)
        adapter.notifyItemInserted(position)
        binding.RecyclerView.post {
            val viewHolder = binding.RecyclerView.findViewHolderForAdapterPosition(position) as MakeListViewHolder?
            viewHolder?.requestFocus()
        }
    }

    private fun setupRecyclerView() {
        adapter = MakeListAdapter(this, model.items)

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val drag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipe = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(drag, swipe)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Collections.swap(model.items, viewHolder.adapterPosition, target.adapterPosition)
                adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                model.items.removeAt(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                adapter.notifyItemRangeChanged(viewHolder.adapterPosition, model.items.size)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.RecyclerView)

        adapter.listItemListener = object : ListItemListener {
            override fun onMoveToNext(position: Int) {
                moveToNext(position)
            }

            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }

            override fun onItemTextChange(position: Int, newText: String) {
                adapter.items[position].body = newText
            }

            override fun onItemCheckedChange(position: Int, checked: Boolean) {
                adapter.items[position].checked = checked
            }
        }

        binding.RecyclerView.adapter = adapter
        binding.RecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun moveToNext(position: Int) {
        val viewHolder = binding.RecyclerView.findViewHolderForAdapterPosition(position + 1) as MakeListViewHolder?
        if (viewHolder != null && !viewHolder.binding.CheckBox.isChecked) {
            viewHolder.requestFocus()
        } else addListItem(position + 1)
    }
}