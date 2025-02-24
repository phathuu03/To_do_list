package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ViewHolderNoteBinding
import com.example.myapplication.entity.NoteWithDetails

class NoteAdapter(
    private val data: MutableList<NoteWithDetails>,
    val onClickItem: (id: Long) -> Unit,
    val onLongClickItem: (View, NoteWithDetails) -> Unit,
    private val context: Context
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(private val binding: ViewHolderNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val title = binding.tvTitle
        private val content = binding.tvContent
        private val image = binding.imgNote
        private val itemChecked = binding.itemChecked
        private val dateStart = binding.tvDate
        private val timeStart = binding.tvTime
        private val recyclerView = binding.recycleViewNoteChecked
        private val layoutItem = binding.layoutNoteItem

        fun bind(noteWithDetails: NoteWithDetails) {
            itemView.setOnClickListener {
                onClickItem(noteWithDetails.note.idNote)
            }
            itemView.setOnLongClickListener {
                onLongClickItem(it, noteWithDetails)
                return@setOnLongClickListener true
            }

            title.text = noteWithDetails.note.title

            if (noteWithDetails.note.content.isBlank()) {
                content.visibility = View.GONE
            } else {
                content.visibility = View.VISIBLE
                content.text = noteWithDetails.note.content
            }


            if (noteWithDetails.attachmentNotes.isEmpty() && noteWithDetails.tasks.isNotEmpty()) {
                layoutItem.setBackgroundResource(R.drawable.bg_item_note_gray)
                image.visibility = View.GONE
                content.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE


                recyclerView.layoutManager = LinearLayoutManager(context)
                val maxHeight = context.resources.getDimensionPixelSize(R.dimen.max_height_task)
                recyclerView.viewTreeObserver.addOnPreDrawListener {
                    val height = recyclerView.height // Lấy chiều cao sau khi RecyclerView đã vẽ xong
                    if (height > maxHeight) {
                        recyclerView.layoutParams.height = maxHeight // Giới hạn chiều cao
                    } else {
                        recyclerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                    true
                }


                recyclerView.adapter = TaskAdapter(noteWithDetails.tasks)



            } else if (noteWithDetails.attachmentNotes.isEmpty() && noteWithDetails.customCanvasList.isNotEmpty()){

                recyclerView.visibility = View.GONE
                image.visibility = View.VISIBLE
                layoutItem.setBackgroundResource(R.drawable.bg_item_note_blue)
                val uriImage: Uri = Uri.parse(noteWithDetails.customCanvasList[0].uri)

                Glide.with(context)
                    .load(uriImage)
//                       .apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(image)
            }

            else if (noteWithDetails.attachmentNotes.isNotEmpty()) {
                recyclerView.visibility = View.GONE
                image.visibility = View.VISIBLE
                layoutItem.setBackgroundResource(R.drawable.bg_item_note_blue)
                val uriImage: Uri = Uri.parse(noteWithDetails.attachmentNotes[0].uri)

                Glide.with(context)
                    .load(uriImage)
//                       .apply(RequestOptions().transform(RoundedCorners(10)))
                    .into(image)


            } else {
                layoutItem.setBackgroundResource(R.drawable.bg_item_note_red)
                image.visibility = View.GONE

            }

            if (noteWithDetails.tasks.isNotEmpty()) {
                itemChecked.visibility = View.VISIBLE
                itemChecked.text = StringBuilder().apply {
                    append("+ ")
                    val isItemChecked = noteWithDetails.tasks.count { it.isChecked }
                    val itemInTasks = noteWithDetails.tasks.size

                    append("$isItemChecked/$itemInTasks")

                    append(" checked item")
                }
            } else {
                itemChecked.visibility = View.GONE
            }

            timeStart.text = noteWithDetails.note.timeStart
            dateStart.text = noteWithDetails.note.dateStart


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder =
            ViewHolderNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(viewHolder)

    }

    override fun getItemCount(): Int = data.size ?: 0

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        data.let {
            holder.bind(it[position])
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateChange(newData: MutableList<NoteWithDetails>) {
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }

}
