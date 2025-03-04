package com.example.myapplication.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderReminderBinding
import com.example.myapplication.entity.NoteWithDetails

class ReminderAdapter(
    private var data: MutableList<NoteWithDetails>,
    private val setAlarm: (NoteWithDetails) -> Unit,
    private val cancelAlarm: (NoteWithDetails) -> Unit,
    private val setCalendar: (NoteWithDetails) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ViewHolderReminder>() {

    inner class ViewHolderReminder(private val binding: ViewHolderReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(noteWithDetails: NoteWithDetails) {
            binding.tvTitle.text = noteWithDetails.note.title

            val calendar = noteWithDetails.note.calendar
            val hour = calendar?.hour ?: 0
            val minute = calendar?.minute ?: 0

            binding.timeReminder.text = StringBuilder().apply {
                append(String.format("%02d:%02d", hour, minute))
                append(if (hour >= 12) " PM" else " AM")

            }

            binding.timeReminder.setOnClickListener {
                setCalendar(noteWithDetails)
            }

            binding.swAlarlam.setOnCheckedChangeListener(null)
            binding.swAlarlam.isChecked = noteWithDetails.note.isReminder

            // Lắng nghe sự kiện mới
            binding.swAlarlam.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setAlarm(noteWithDetails)
                } else {
                    cancelAlarm(noteWithDetails)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReminder {
        val view =
            ViewHolderReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderReminder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolderReminder, position: Int) {
        holder.bind(data[position])
    }

    /**
     * Cập nhật danh sách bằng DiffUtil để tăng hiệu suất
     */
    fun updateChanged(newData: List<NoteWithDetails>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = data.size
            override fun getNewListSize(): Int = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].note.idNote == newData[newItemPosition].note.idNote
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data = newData.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }
}
