package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.bottomsheet.ChooseAttachmentBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseCategoryBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseFontBottomSheet
import com.example.myapplication.databinding.ActivityAddNoteBinding
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.model.FontNote
import com.example.myapplication.model.Task
import com.example.myapplication.viewmodel.CategoryViewModel
import com.example.myapplication.viewmodel.DateTimeViewModel
import com.example.myapplication.viewmodel.NoteFontViewModel


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
    private var fontNote: FontNote? = null
    private lateinit var noteFontViewModel: NoteFontViewModel
    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var spannable: SpannableStringBuilder
    private val tasks : MutableList<Task>? = null
    private var category: CategoryNote? = null


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





        val typeface = resources.getFont(R.font.roboto_medium)
        tvTime.typeface = typeface
        btnChooseFont.setOnClickListener {
            clickOpenFragmentChooseFont()
        }
        btnChooseAttachment.setOnClickListener {
            openChooseAttachment()
        }
        btnChooseCategory.setOnClickListener {
            openChooseCategoryFragment()
        }
        binding.btnInsertCheckBox.setOnClickListener {
            insertCheckBox()
        }
        binding.btnChooseAttachment.setOnClickListener {
            insertImageOrVideoInNote()
        }
    }

    private fun defaultCategories() {
        val listCategory  =  listOf<CategoryNote>(
            CategoryNote(nameCategory = "bank"),
            CategoryNote(nameCategory = "study"),
            CategoryNote(nameCategory = "alam")
        )
        categoryViewModel.insertCategory(listCategory)
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


    override fun onStart() {
        super.onStart()


    }




    @SuppressLint("UseCompatLoadingForDrawables")
    private fun insertCheckBox() {
        val uncheckedBox = "\u2610" // ☐
//        val checkedBox = "\u2611" // ☑

        var nameTask = ""
        tasks?.add(Task(nameTask = "Task " ))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.enter_task_name))

        val input = EditText(this)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            val enteredText = input.text.toString()  // Lấy văn bản người dùng nhập
            // Bạn có thể sử dụng `enteredText` ở đây
            editContent.append("\n$uncheckedBox  $enteredText \n")
            tasks?.add(Task(nameTask= enteredText))


        }
        builder.show()

//        editContent.append("$checkedBox Task 2\n")

    }
    private fun insertImageOrVideoInNote(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)

    }
    private fun changeFontNote() {
        var textInitial: String =""
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
            }else{
                editContent.paintFlags = editContent.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }

            if(font.isFontSample){
                editContent.setText(editContent.text.toString().toUpperCase())
            }else{
                textInitial = editContent.text.toString()
                editContent.setText(textInitial)
                editContent.setText(editContent.text.toString().toLowerCase())
            }

        }
    }


    private fun openChooseCategoryFragment() {
        val bottomSheetChooseCategoryBottomSheetFragment = ChooseCategoryBottomSheetFragment(categoryViewModel)
        bottomSheetChooseCategoryBottomSheetFragment.show(
            supportFragmentManager, bottomSheetChooseCategoryBottomSheetFragment.tag
        )
    }

    private fun openChooseAttachment() {
        val bottomSheetFragmentAttachment = ChooseAttachmentBottomSheetFragment()
        bottomSheetFragmentAttachment.show(
            supportFragmentManager, bottomSheetFragmentAttachment.tag
        )
    }

    private fun initView() {
        btnChooseFont = binding.btnChooseFont
        btnChooseAttachment = binding.btnChooseAttachment
        btnChooseCategory = binding.layoutCategory
        tvDate = binding.tvDate
        tvTime = binding.tvTime
        btnShowCategory = binding.btnShowCategory
        editTitle = binding.editTitleNote
        editContent = binding.editContentNote
        spannable = SpannableStringBuilder(editContent.text)
        noteFontViewModel = ViewModelProvider(this)[NoteFontViewModel::class.java]
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

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

    override fun onResume() {
        super.onResume()

    }


}