package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
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
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.bottomsheet.BrushCanvasBottomSheet
import com.example.myapplication.bottomsheet.ChooseAttachmentBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseCategoryBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseEmojiBottomSheet
import com.example.myapplication.bottomsheet.ChooseFontBottomSheet
import com.example.myapplication.bottomsheet.RecorderBottomSheetFragment
import com.example.myapplication.dao.NoteDao
import com.example.myapplication.database.DatabaseBuilder
import com.example.myapplication.databinding.ActivityAddNoteBinding
import com.example.myapplication.entity.NoteEntity
import com.example.myapplication.listener.PasserEmoji
import com.example.myapplication.model.AttachmentNote
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.model.FontNote
import com.example.myapplication.model.Task
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.AttachmentNoteViewModel
import com.example.myapplication.viewmodel.CanvasViewModel
import com.example.myapplication.viewmodel.CategoryViewModel
import com.example.myapplication.viewmodel.DateTimeViewModel
import com.example.myapplication.viewmodel.NoteFontViewModel
import com.example.myapplication.viewmodel.NoteViewModel
import com.example.myapplication.viewmodel.RecorderViewModel
import com.example.myapplication.viewmodelfactory.NoteViewModelFactory
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.launch


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
    private val tasks: MutableList<Task> = mutableListOf()
    private val listCategory = mutableListOf<CategoryNote>()
    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepository
    private lateinit var noteViewModel: NoteViewModel


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
            checkPermissionRecorderAttachment()
        }
        btnAddRecorder.setOnClickListener {
            checkPermissionRecorder()
        }
        binding.btnAddBrush.setOnClickListener {
            openCanvas()
        }
        binding.btnChooseEmoji.setOnClickListener {
            openChooseEmoji()

        }

    }

    private fun checkPermissionRecorderAttachment() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openChooseAttachBottomSheet()

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

            }


        }

        TedPermission.create().setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.CAMERA).check();

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


        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.enter_task_name))

        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            val enteredText = input.text.toString()
            tasks.add(Task(nameTask = enteredText))


            editContent.append("\n$uncheckedBox  $enteredText \n")


        }
        builder.show()


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
            val typeface: Typeface? = ResourcesCompat.getFont(this, font.resId)
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
        noteDao = DatabaseBuilder.getInstance(application).noteDao()
        repository = NoteRepository(noteDao)
        noteViewModel =
            ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]

        noteViewModel.allNotes.observe(this) {
            Log.d("data", "dfdfads")
        }


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

            R.id.note_save_note -> {
                saveNote()
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun saveNote() {
    noteFontViewModel.font.observe(this){
        this.fontNote = it
    }



        val note = NoteEntity(
            dateStart = tvDate.text.toString(),
            timeStart = tvDate.text.toString(),
            timeReminder = null,
            timeUpdate = null,
            category = null,
            title = editTitle.text.toString(),
            font = fontNote!!,
            content = editContent.text.toString(),
            isFavorite = true
        )


        lifecycleScope.launch {
            val id = noteViewModel.insertOrUpdateNote(note)
            Log.d("data id", id.toString())

        }
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
            .also {
                startActivity(it)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    private fun checkPermissionRecorder() {
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