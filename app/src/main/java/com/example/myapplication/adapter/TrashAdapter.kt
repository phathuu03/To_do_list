import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderTrashBinding
import com.example.myapplication.entity.NoteEntity

class TrashAdapter(
    val data: MutableList<NoteEntity>,
    val selectedItems: MutableSet<NoteEntity>,
    val onSelectedItem: (NoteEntity) -> Unit,
    val onUnSelectedItem: (NoteEntity) -> Unit
) : RecyclerView.Adapter<TrashAdapter.ViewHolderTrash>() {

    inner class ViewHolderTrash(val binding: ViewHolderTrashBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(noteEntity: NoteEntity) {
            binding.tvTitle.text = noteEntity.title
            binding.tvDateStart.text = "${noteEntity.dateStart}, ${noteEntity.timeStart}"

            // Kiểm tra xem noteEntity có trong danh sách đã chọn hay không
            binding.rdSelectedItem.setOnCheckedChangeListener(null) // Xóa listener cũ để tránh lỗi
            binding.rdSelectedItem.isChecked = selectedItems.contains(noteEntity)

            // Bắt sự kiện khi người dùng click vào RadioButton
            binding.rdSelectedItem.setOnClickListener {
                if (selectedItems.contains(noteEntity)) {
                    selectedItems.remove(noteEntity) // Bỏ chọn
                    onUnSelectedItem(noteEntity)
                } else {
                    selectedItems.add(noteEntity) // Chọn
                    onSelectedItem(noteEntity)
                }
                notifyItemChanged(adapterPosition) // Cập nhật lại trạng thái
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrash {
        val view = ViewHolderTrashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTrash(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolderTrash, position: Int) {
        holder.bind(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataChanged(newDt: List<NoteEntity>) {
        this.data.clear()
        this.data.addAll(newDt)
        notifyDataSetChanged()
    }
}
