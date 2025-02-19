package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
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
import com.example.myapplication.bottomsheet.BrushCanvasBottomSheet
import com.example.myapplication.bottomsheet.ChooseAttachmentBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseCategoryBottomSheetFragment
import com.example.myapplication.bottomsheet.ChooseEmojiBottomSheet
import com.example.myapplication.bottomsheet.ChooseFontBottomSheet
import com.example.myapplication.bottomsheet.RecorderBottomSheetFragment
import com.example.myapplication.dao.NoteDao
import com.example.myapplication.database.DatabaseBuilder
import com.example.myapplication.databinding.ActivityAddNoteBinding
import com.example.myapplication.entity.AttachmentNoteEntity
import com.example.myapplication.entity.AudioRecordEntity
import com.example.myapplication.entity.CategoryEntity
import com.example.myapplication.entity.CategoryStringEntity
import com.example.myapplication.entity.CustomCanvasEntity
import com.example.myapplication.entity.NoteEntity
import com.example.myapplication.entity.TaskEntity
import com.example.myapplication.listener.PasserCategory
import com.example.myapplication.listener.PasserEmoji
import com.example.myapplication.model.AttachmentNote
import com.example.myapplication.model.Calendar
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.model.FontNote
import com.example.myapplication.model.Task
import com.example.myapplication.reciver.AlarmReceiver
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.AttachmentNoteViewModel
import com.example.myapplication.viewmodel.CanvasViewModel
import com.example.myapplication.viewmodel.CategoryViewModel
import com.example.myapplication.viewmodel.DateTimeViewModel
import com.example.myapplication.viewmodel.NoteFontViewModel
import com.example.myapplication.viewmodel.NoteViewModel
import com.example.myapplication.viewmodel.RecorderViewModel
import com.example.myapplication.viewmodelfactory.NoteViewModelFactory
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit


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
    private var attachmentNotes: MutableList<AttachmentNote> = mutableListOf()
    private lateinit var noteFontViewModel: NoteFontViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var attachmentNoteViewModel: AttachmentNoteViewModel
    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var canvasViewModel: CanvasViewModel
    private var isFavorite: Boolean = false
    private lateinit var spannable: SpannableStringBuilder
    private val tasks: MutableList<Task> = mutableListOf()
    private val listCategory = mutableListOf<CategoryNote>()
    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepository
    private lateinit var noteViewModel: NoteViewModel
    private var idNote: Long = 0
    private val listNameCategory: MutableList<CategoryStringEntity> = mutableListOf()
    private var calendarAlarm: Calendar? = null
    private var isSaveOrAdd = false
    private var timeUpdate: LocalDateTime? = null
    private val listAttachmentEntity = mutableListOf<AttachmentNoteEntity>()
    private val listRecorder = mutableListOf<AudioRecordEntity>()
    private val listCustomCanvas = mutableListOf<CustomCanvasEntity>()
    private var isArchive = false
    private var isTrash = false



    private val binding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        checkBundle()
        if(isSaveOrAdd){
            setUpViewModelFont()
            setDateTime()

        }
        createNotificationChannel()
        setUpToolBar()
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

    private fun checkBundle() {
        idNote = intent.getLongExtra("id_note", -1L)
        if (idNote == -1L) {
            isSaveOrAdd = true
        } else {
            isSaveOrAdd = false
            initViewUpdate(idNote)
        }
    }

    private fun initViewUpdate(idNote: Long) {
        noteViewModel.getNoteWithDetails(idNote)
        noteViewModel.noteWithDetails.observe(this) {
            if (it != null) {
                editTitle.setText(it.note.title)
                editContent.setText(it.note.content)
                this.timeUpdate = LocalDateTime.now()
                this.fontNote = it.note.font
                setUpViewModelFont()
                this.isTrash = it.note.isTrash
                this.isArchive = it.note.isArchive
                this.tvDate.text = it.note.dateStart
                this.tvTime.text = it.note.timeStart
                this.isFavorite= it.note.isFavorite



            } else {
                isSaveOrAdd = true
            }

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
            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .check();

    }


    private fun openChooseEmoji() {
        val chooseEmojiBottomSheet = ChooseEmojiBottomSheet(object : PasserEmoji {
            override fun passEmoji(emoji: String): Unit {
                val startSelection = editContent.selectionStart
                editContent.text.insert(startSelection, emoji)
            }
        })
        chooseEmojiBottomSheet.show(supportFragmentManager, chooseEmojiBottomSheet.tag)


    }
    private fun setUpViewModelFont() {
        if (this.fontNote == null) {
            noteFontViewModel.setFontDefault()
        } else {
            noteFontViewModel.setFontCustom(this.fontNote!!)
        }
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
            supportFragmentManager, chooseAttachmentBottomSheetFragment.tag
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
            ChooseCategoryBottomSheetFragment(noteViewModel, object : PasserCategory {
                override fun passerCategory(categoryStringEntity: CategoryStringEntity) {
                    listNameCategory.add(categoryStringEntity)
                }

            }

            )
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

            R.id.note_alarm -> {

                chooseCalender()
                return true
            }

            R.id.note_favorite -> {
                isFavorite = !isFavorite
                val icDrawble = item.icon
                icDrawble?.let {
                    val color = if (isFavorite) Color.RED else Color.GRAY
                    it.setTint(color)
                    item.setIcon(it)
                }
                return true
            }

            R.id.note_save_note -> {
                saveNote()
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun chooseCalender() {

        val timePicker =
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(LocalTime.now().hour)
                .setMinute(LocalTime.now().minute).setTitleText("Select Alarm Time").build()
        timePicker.show(supportFragmentManager, "")

        timePicker.addOnPositiveButtonClickListener {
            val calender = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, timePicker.hour)
                set(java.util.Calendar.MINUTE, timePicker.minute)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)

            }

            calendarAlarm = Calendar(
                timestampL = calender.timeInMillis,
                hour =TimeUnit.HOURS.toHours(calender.timeInMillis),
                minute = TimeUnit.MINUTES.toMinutes(calender.timeInMillis  / 60)
            )

        }

    }


    private fun saveNote() {
        if (editTitle.text.isNullOrBlank()){
            Toast.makeText(this, "Title is not blank", Toast.LENGTH_SHORT).show()
            return
        }
        if(editContent.text.isNullOrBlank()) {
            Toast.makeText(this, "Content is not blank", Toast.LENGTH_SHORT).show()
            return
        }

        noteFontViewModel.font.observe(this) {
            this.fontNote = it
        }

        if (isSaveOrAdd) {
            val note = NoteEntity(
                dateStart = tvDate.text.toString(),
                timeStart = tvTime.text.toString(),
                timeReminder = null,
                timeUpdate = this.timeUpdate,
                title = editTitle.text.toString(),
                font = this.fontNote!!,
                calendar = calendarAlarm,
                content = editContent.text.toString(),
                isFavorite = isFavorite,
                isArchive = this.isArchive,
                isTrash = this.isTrash
            )
            noteViewModel.insertOrUpdateNote(note)
        } else {
            val note = NoteEntity(
                idNote = idNote,
                dateStart = tvDate.text.toString(),
                timeStart = tvTime.text.toString(),
                timeReminder = null,
                timeUpdate = LocalDateTime.now(),
                title = editTitle.text.toString(),
                font = this.fontNote!!,
                calendar = calendarAlarm,
                content = editContent.text.toString(),
                isFavorite = isFavorite,
                isArchive = this.isArchive,
                isTrash = this.isTrash
            )
            noteViewModel.insertOrUpdateNote(note)

        }




        noteViewModel.idNote.observe(this) { idNote ->
            this.idNote = idNote


            attachmentNoteViewModel.attachmentNotes.observe(this) {
                it?.forEach { att ->

                    val attachmentNoteEntity = AttachmentNoteEntity(
                        noteId = this.idNote, uri = att.uri, type = att.type
                    )
                    listAttachmentEntity.add(attachmentNoteEntity)
                }


            }
            // them attachment
            if (listAttachmentEntity.isNotEmpty()) {
                listAttachmentEntity.forEach {
                    noteViewModel.insertOrUpdateAttachment(it)
                }
            }
            //task list

            this.tasks.let { tasks ->
                tasks.forEach { task ->
                    val taskEntity = TaskEntity(
                        noteId = idNote,
                        nameTask = task.nameTask,
                    )
                    noteViewModel.addTaskEntity(taskEntity)
                }

            }

            recorderViewModel.recorders.observe(this) { recorder ->

                recorder?.forEach { itemRecorder ->
                    val audioRecordEntity = AudioRecordEntity(
                        noteId = idNote, fileName = itemRecorder.fileName, uri = itemRecorder.uri
                    )
                    listRecorder.add(audioRecordEntity)

                }

            }
            // alarm

            calendarAlarm?.let {
                setAlarm(it.timestampL)
            }


            canvasViewModel.canvas.observe(this) { canvas ->

                canvas.forEach {
                    val canvasEntity = CustomCanvasEntity(
                        noteId = idNote, fileName = it.fileName, uri = it.uri
                    )
                    listCustomCanvas.add(canvasEntity)
                }

            }

            // them can vas
            if (listCustomCanvas.isNotEmpty()) {
                listCustomCanvas.forEach {
                    noteViewModel.insertCustomerCanvas(it)
                }
            }

            // them recorder
            if (listRecorder.isNotEmpty()) {
                listRecorder.forEach { recorder ->
                    noteViewModel.insertAudioRecorder(recorder)
                }
            }




            // them category
            if (listNameCategory.isNotEmpty()) {
                listNameCategory.forEach { nameCategory ->
                    noteViewModel.insertCategory(
                        CategoryEntity(
                            noteId = idNote, nameCategory = nameCategory.nameCategory
                        )
                    )
                }
            }



            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }




    }

    private fun setAlarm(timestampL: Long) {

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(
            this, AlarmReceiver::class.java
        )

        intent.putExtra("id_note", this.idNote)
        intent.putExtra("title_note", editTitle.text.toString())
        intent.putExtra("content_note", editContent.text.toString())

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, timestampL, AlarmManager.INTERVAL_DAY, pendingIntent
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)

        val itemMenu = menu?.findItem(R.id.note_favorite)
        val drawable = itemMenu?.icon
        drawable?.let {
            val color = if (isFavorite) Color.RED else Color.GRAY
            it.setTint(color)
            itemMenu.setIcon(it)
        }
        return super.onCreateOptionsMenu(menu)
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


    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "akchannel"
            val desc = "Channel for Alarm Manager"
            val imp = NotificationManager.IMPORTANCE_MAX
            val channel =
                NotificationChannel("androidknowledge", name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = desc
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }


}