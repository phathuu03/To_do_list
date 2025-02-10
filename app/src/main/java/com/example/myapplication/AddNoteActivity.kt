package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.bottomsheet.BrushCanvasBottomSheet
import com.example.myapplication.bottomsheet.ChooseAttachmentBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseCategoryBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseEmojiBottomSheet
import com.example.myapplication.bottomsheet.ChooseFontBottomSheet
import com.example.myapplication.bottomsheet.RecorderBottomSheetFragment
import com.example.myapplication.databinding.ActivityAddNoteBinding
import com.example.myapplication.listener.PasserEmoji
import com.example.myapplication.model.AttachmentNote
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.model.FontNote
import com.example.myapplication.model.NoteItem
import com.example.myapplication.model.Task
import com.example.myapplication.viewmodel.AttachmentNoteViewModel
import com.example.myapplication.viewmodel.CanvasViewModel
import com.example.myapplication.viewmodel.CategoryViewModel
import com.example.myapplication.viewmodel.DateTimeViewModel
import com.example.myapplication.viewmodel.NoteFontViewModel
import com.example.myapplication.viewmodel.RecorderViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class AddNoteActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var btnChooseFont: ImageButton
    private lateinit var btnChooseAttachment: ImageButton
    private lateinit var btnChooseCategory: RelativeLayout
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnShowCategory: ImageView
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var btnAddRecorder: ImageButton
    private var fontNote: FontNote? = null
    private var attachmentNotes: MutableList<AttachmentNote>? = null
    private lateinit var noteFontViewModel: NoteFontViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var attachmentNoteViewModel: AttachmentNoteViewModel
    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var canvasViewModel: CanvasViewModel

    private lateinit var spannable: SpannableStringBuilder
    private val tasks: MutableList<Task>? = null
    private val listCategory = mutableListOf<CategoryNote>()


    private val binding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)

    }

    private fun setUpViewModelFont() {

        if (fontNote == null) {
            noteFontViewModel.setFontDefault()
        } else {
            noteFontViewModel.setFontCustom(this.fontNote!!)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setUpToolBar()
        setUpViewModelFont()
        setDateTime()
        changeFontNote()
        defaultCategories()
        setUpRecyclerView()
        attachmentNoteViewModel.attachmentNotes.observe(this) {
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
        }


        val typeface = resources.getFont(R.font.roboto_medium)
        tvTime.typeface = typeface
        btnChooseFont.setOnClickListener {
            clickOpenFragmentChooseFont()
        }

        btnChooseCategory.setOnClickListener {
            openChooseCategoryFragment()
        }
        binding.btnInsertCheckBox.setOnClickListener {
            insertCheckBox()
        }
        binding.btnChooseAttachment.setOnClickListener {
            openChooseAttachBottomSheet()
        }
        btnAddRecorder.setOnClickListener {
            checkPermission()
        }
        binding.btnAddBrush.setOnClickListener {
            openCanvas()
        }
        binding.btnChooseEmoji.setOnClickListener {
            openChooseEmoji()

        }

    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewAddNote
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Khởi tạo adapter 1 lần duy nhất
        val adapter = NoteAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // Lắng nghe dữ liệu từ ViewModel
        canvasViewModel.canvas.observe(this) { canvasList ->
            if (canvasList.isNotEmpty()) {
                val data = mutableListOf<NoteItem>()

                // Thêm dữ liệu từ ViewModel vào danh sách
                canvasList.forEach { canvas ->
                    data.add(NoteItem.CanvasItem(canvas))
                }

                // Cập nhật dữ liệu trong adapter mà không tạo adapter mới
                adapter.updateData(data)
            }
        }
    }


    private fun openChooseEmoji() {
        val chooseEmojiBottomSheet = ChooseEmojiBottomSheet(object : PasserEmoji {
            override fun passEmoji(emoji: String): Unit {
                Log.d("data emoji", emoji)
            }
        })
        chooseEmojiBottomSheet.show(supportFragmentManager, chooseEmojiBottomSheet.tag)


    }

    private fun openCanvas() {
        val brushCanvasBottomSheet = BrushCanvasBottomSheet(canvasViewModel)
        brushCanvasBottomSheet.show(supportFragmentManager, brushCanvasBottomSheet.tag)
    }

    private fun openAddRecorderBottomSheet() {
        val recorderBottomSheetFragment = RecorderBottomSheetFragment(recorderViewModel)
        recorderBottomSheetFragment.show(supportFragmentManager, recorderBottomSheetFragment.tag)
    }

    private fun defaultCategories() {

        categoryViewModel.listCategory.observe(this) {
            this.listCategory.addAll(it)
        }
    }

    private fun setDateTime() {
        val dateTimeViewModel = ViewModelProvider(this)[DateTimeViewModel::class.java]
        dateTimeViewModel.dateNow.observe(this) {
            tvDate.text = it
        }

        dateTimeViewModel.timeNow.observe(this) {
            tvTime.text = it
        }


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun insertCheckBox() {
        val uncheckedBox = "\u2610" // ☐
//        val checkedBox = "\u2611" // ☑

        var nameTask = ""
        tasks?.add(Task(nameTask = "Task "))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.enter_task_name))

        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            val enteredText = input.text.toString()  // Lấy văn bản người dùng nhập
            // Bạn có thể sử dụng `enteredText` ở đây
            editContent.append("\n$uncheckedBox  $enteredText \n")
            tasks?.add(Task(nameTask = enteredText))


        }
        builder.show()


    }

    private fun insertImageInEditText(uriImage: Uri) {
        var cursorPosition = editContent.selectionStart

        // 🔹 Nếu `EditText` trống, chèn khoảng trắng trước
        if (editContent.text.isEmpty()) {
            editContent.setText(" ") // Để có ít nhất một ký tự, tránh lỗi IndexOutOfBounds
            cursorPosition = 1 // Đặt con trỏ sau ký tự đầu tiên
        }

        // 🔹 Đảm bảo con trỏ không vượt quá độ dài văn bản
        if (cursorPosition < 0 || cursorPosition > editContent.text.length) {
            cursorPosition = editContent.text.length
        }

        val drawable: Drawable? = try {
            contentResolver.openInputStream(uriImage)?.use {
                Drawable.createFromStream(it, uriImage.toString())
            }
        } catch (e: Exception) {
            null
        }

        if (drawable != null) {
            val maxWidth = (editContent.width * 0.9).toInt() // 90% chiều rộng EditText
            val aspectRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()
            val newHeight = (maxWidth / aspectRatio).toInt()

            drawable.setBounds(0, 0, maxWidth, newHeight)

            val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM) // Căn giữa ảnh

            val editableText = editContent.editableText

            // 🔹 Chèn ảnh an toàn (chèn khoảng trắng nếu cần)
            if (cursorPosition + 1 > editableText.length) {
                editableText.insert(
                    cursorPosition,
                    " \n"
                ) // Thêm khoảng trắng để không vượt quá độ dài
            }

            editableText.setSpan(
                imageSpan,
                cursorPosition,
                cursorPosition + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // 🔹 Đảm bảo có khoảng trắng phía dưới ảnh để tách biệt với văn bản
            editableText.insert(cursorPosition + 1, "\n")

            // 🔹 Cập nhật con trỏ
            editContent.setSelection(cursorPosition + 2)
        }
    }


    private fun openChooseAttachBottomSheet() {
        val chooseAttachmentBottomSheetFragment =
            ChooseAttachmentBottomSheetFragment(attachmentNoteViewModel)
        chooseAttachmentBottomSheetFragment.show(
            supportFragmentManager,
            chooseAttachmentBottomSheetFragment.tag
        )

    }

    private fun changeFontNote() {
        var textInitial: String = ""
        noteFontViewModel.font.observe(this) { font ->
            editContent.textSize = font.fontSize.toFloat()
            val typeface: Typeface? = ResourcesCompat.getFont(this, font.pathFont.resId)
            editContent.setTypeface(typeface)
            if (font.isItalic) {
                editContent.setTypeface(editContent.typeface, Typeface.ITALIC)
            }
            if (font.isBold) {
                editContent.setTypeface(editContent.typeface, Typeface.BOLD)
            }
            if (font.isBold) {
                if (font.isItalic) {
                    editContent.setTypeface(editContent.typeface, Typeface.BOLD_ITALIC)
                } else {
                    editContent.setTypeface(editContent.typeface, Typeface.BOLD)
                }
            }
            if (font.isUnderscoreL) {
                editContent.paintFlags = editContent.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            } else {
                editContent.paintFlags = editContent.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }

            if (font.isFontSample) {
                editContent.setText(editContent.text.toString().toUpperCase())
            } else {
                textInitial = editContent.text.toString()
                editContent.setText(textInitial)
                editContent.setText(editContent.text.toString().toLowerCase())
            }

        }
    }


    private fun openChooseCategoryFragment() {
        val bottomSheetChooseCategoryBottomSheetFragment =
            ChooseCategoryBottomSheetFragment(categoryViewModel)
        bottomSheetChooseCategoryBottomSheetFragment.show(
            supportFragmentManager, bottomSheetChooseCategoryBottomSheetFragment.tag
        )
    }


    private fun initView() {
        btnChooseFont = binding.btnChooseFont
        btnChooseAttachment = binding.btnChooseAttachment
        btnChooseCategory = binding.layoutCategory
        btnAddRecorder = binding.btnAddRecorder
        tvDate = binding.tvDate
        tvTime = binding.tvTime
        btnShowCategory = binding.btnShowCategory
        editTitle = binding.editTitleNote
        editContent = binding.editContentNote
        spannable = SpannableStringBuilder(editContent.text)
        noteFontViewModel = ViewModelProvider(this)[NoteFontViewModel::class.java]
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        attachmentNoteViewModel = ViewModelProvider(this)[AttachmentNoteViewModel::class.java]
        recorderViewModel = ViewModelProvider(this)[RecorderViewModel::class.java]
        canvasViewModel = ViewModelProvider(this)[CanvasViewModel::class.java]

    }

    private fun clickOpenFragmentChooseFont() {
        val bottomSheetFragment = ChooseFontBottomSheet(noteFontViewModel)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

    }


    private fun setUpToolBar() {
        toolbar = binding.toolbarAdd
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    private fun checkPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openAddRecorderBottomSheet()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

            }


        }

        TedPermission.create().setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.RECORD_AUDIO).check();
    }

    override fun onResume() {
        super.onResume()

    }


}