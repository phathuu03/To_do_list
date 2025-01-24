package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.bottomsheet.ChooseAttachmentBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseCategoryBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseFontBottomSheet
import com.example.myapplication.databinding.ActivityAddNoteBinding
import com.example.myapplication.listener.PasserFontNote
import com.example.myapplication.model.FontNote
import com.example.myapplication.viewmodel.NoteFontViewModel


class AddNoteActivity : AppCompatActivity(), PasserFontNote {
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


    private val binding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }

    private fun setUpViewModelFont() {
        val viewModel = ViewModelProvider(this)[NoteFontViewModel::class.java]

        if (fontNote == null) {
            viewModel.setFontDefault()
        } else {
            viewModel.setFontCustom(this.fontNote!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpToolBar()
        setUpViewModelFont()
        initView()


        tvDate.setOnClickListener {
            customerEditContentText()
        }
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
    }

    override fun onStart() {
        super.onStart()
    }




    private fun customerEditContentText() {
        insertImage()


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun insertImage() {
        val spannable = SpannableStringBuilder(editContent.text)

        val drawable = getDrawable(R.drawable.img_remider_empty)!!
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        if (spannable.isEmpty()) {
            spannable.append(" fafsdfas")
        }
        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
        val start = editContent.selectionStart
        spannable.insert(start, " "); // Thêm khoảng trắng tại vị trí con trỏ

        spannable.setSpan(imageSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        editContent.text = spannable

        val uncheckedBox = "\u2610" // ☐
        val checkedBox = "\u2611" // ☑


        editContent.append("$uncheckedBox Task 1\n")
        editContent.append("$checkedBox Task 2\n")

    }


    private fun openChooseCategoryFragment() {
        val bottomSheetChooseCategoryBottomSheetFragment = ChooseCategoryBottomSheetFragment()
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
    }

    private fun clickOpenFragmentChooseFont() {
        val bottomSheetFragment = ChooseFontBottomSheet(this)
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

    override fun passerFontNote(fontNote: FontNote?) {
        this.fontNote = fontNote
        setUpViewModelFont()
    }


}