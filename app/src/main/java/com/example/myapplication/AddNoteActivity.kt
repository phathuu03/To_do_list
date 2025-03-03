package com.example.myapplication

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AttachmentAdapter
import com.example.myapplication.adapter.CanvasAdapter
import com.example.myapplication.adapter.RecorderAdapter
import com.example.myapplication.adapter.TaskInAddNoteAdapter
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
import com.example.myapplication.model.AudioRecord
import com.example.myapplication.model.Calendar
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.model.CustomCanvas
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
import java.io.IOException
import java.time.LocalDateTime
import java.time.LocalTime


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
    private lateinit var recyclerView: RecyclerView
    private var fontNote: FontNote? = null
    private lateinit var noteFontViewModel: NoteFontViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var attachmentNoteViewModel: AttachmentNoteViewModel
    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var canvasViewModel: CanvasViewModel
    private var isFavorite: Boolean = false
    private lateinit var spannable: SpannableStringBuilder
    private var tasks = mutableListOf<Task>()
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
    private var isTaskList = false
    private lateinit var adapter: TaskInAddNoteAdapter
    private lateinit var recorderAdapter: RecorderAdapter

    private lateinit var adapterAttachment: AttachmentAdapter
    private lateinit var canvasAdapter: CanvasAdapter
    private lateinit var recyclerViewAttachment: RecyclerView
    private lateinit var recyclerViewCanvas: RecyclerView
    private lateinit var recyclerViewAudio: RecyclerView
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0

    private val listTaskDel = mutableListOf<Task>()

    private val listAttDel = mutableListOf<AttachmentNoteEntity>()
    private val listAttAdd = mutableListOf<AttachmentNoteEntity>()

    private val listCanvasDel = mutableListOf<CustomCanvasEntity>()
    private val listCanvasAdd = mutableListOf<CustomCanvasEntity>()

    private val listRecordDel = mutableListOf<AudioRecordEntity>()
    private val listRecordAdd = mutableListOf<AudioRecordEntity>()

    var isReminder: Boolean = false


    private val binding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        checkBundle()
        if (isSaveOrAdd) {
            setUpViewModelFont()
            setDateTime()

        }
        createNotificationChannel()
        setUpToolBar()
        changeFontNote()
        defaultCategories()


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
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun initViewAttment() {

        attachmentNoteViewModel.attachmentNotes.observe(this) {
            if (it.isEmpty()) {
                recyclerViewAttachment.visibility = View.GONE
            } else {
                recyclerViewAttachment.visibility = View.VISIBLE


                adapterAttachment = AttachmentAdapter(
                    data = it.toMutableList(),
                    onDelAtt = { att ->
                        val attment = att as AttachmentNote
                        attachmentNoteViewModel.removeAttachment(attment)
                    },
                    context = this
                )
                recyclerViewAttachment.adapter = adapterAttachment

            }
        }

    }

    private fun initViewRecorder() {
        recorderViewModel.recorders.observe(this) {
            if (it.isEmpty()) {
                recyclerViewAudio.visibility = View.GONE
            } else {
                recyclerViewAudio.visibility = View.VISIBLE
                recorderAdapter = RecorderAdapter(
                    data = it.toMutableList(),
                    onClickItem = { task ->
                        if (task is AudioRecord)
                            if (task.isPlaying) {
                                pauseRecorder()
                            } else {
                                playRecorder(task.uri)
                            }
                    },
                    onDeleteRecorder = { recorder ->
                        if (recorder is AudioRecord)
                            recorderViewModel.removeRecorder(recorder)
                    }
                )
                recyclerViewAudio.adapter = recorderAdapter

            }
        }
    }

    private fun pauseRecorder() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                currentPosition = it.currentPosition
                it.pause()
            }
        }
    }


    private fun playRecorder(audioUri: String) {
        if (audioUri.isNotEmpty()) {
            try {
                val uriMedia = Uri.parse(audioUri)

                mediaPlayer?.let {
                    if (it.isPlaying) {
                        it.stop()  // Dừng nếu đang phát
                    }
//                    it.reset()
                } ?: run {
                    mediaPlayer = MediaPlayer()
                }

                mediaPlayer?.apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )

                    setDataSource(this@AddNoteActivity, uriMedia)
                    prepareAsync() // Sử dụng prepareAsync để tránh làm treo UI
                    setOnPreparedListener {
                        if (currentPosition > 0) {
                            seekTo(currentPosition)
                        }
                        start()
                    }


                }

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    this@AddNoteActivity, "Error playing audio: ${e.message}", Toast.LENGTH_SHORT
                ).show()
                Log.e("MediaPlayer", "Error playing audio", e)
            }
        } else {
            Toast.makeText(this@AddNoteActivity, "URI is null or empty", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Giải phóng tài nguyên khi activity bị hủy
        mediaPlayer = null
    }

    private fun checkBundle() {
        idNote = intent.getLongExtra("id_note", -1L)
        if (idNote == -1L) {
            isSaveOrAdd = true
            initViewAttment()
            initViewCanvas()
            initViewRecorder()
        } else {
            isSaveOrAdd = false
            initViewUpdate(idNote)

        }
    }

    private fun saveAtt(idNote: Long) {
        attachmentNoteViewModel.attachmentNotes.observe(this) {
            if (it.isNotEmpty()) {
                val attEntity = AttachmentNoteEntity(
                    idAttachment = it[it.size - 1].idAttachment,
                    noteId = idNote,
                    uri = it[it.size - 1].uri,
                    type = it[it.size - 1].type
                )
                noteViewModel.insertOrUpdateAttachment(attEntity)
                noteViewModel.getNoteWithDetails(idNote)
                listAttAdd.add(attEntity)
            }

        }

    }

    private fun saveCanvas(idNote: Long) {
        canvasViewModel.canvas.observe(this) {
            if (it.isNotEmpty()) {

                val canvasEntity = CustomCanvasEntity(
                    idCanvas = it[it.size - 1].id,
                    noteId = idNote,
                    fileName = it[it.size - 1].fileName,
                    uri = it[it.size - 1].uri
                )
                noteViewModel.insertCustomerCanvas(canvasEntity)
                noteViewModel.getNoteWithDetails(idNote)
                listCanvasAdd.add(canvasEntity)
            }

        }
    }


    private fun saveRecorder(idNote: Long) {
        recorderViewModel.recorders.observe(this) {
            if (it.isNotEmpty()) {
                val recorderEntity = AudioRecordEntity(
                    idAudio = it[it.size - 1].id,
                    noteId = idNote,
                    fileName = it[it.size - 1].fileName,
                    uri = it[it.size - 1].uri,
                    isPlaying = it[it.size - 1].isPlaying
                )

                noteViewModel.insertAudioRecorder(recorderEntity)
                noteViewModel.getNoteWithDetails(idNote)
                listRecordDel.add(recorderEntity)
            }
        }
    }

    private fun initViewUpdate(idNote: Long) {
        saveAtt(idNote)
        saveCanvas(idNote)
        saveRecorder(idNote)
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
                this.isFavorite = it.note.isFavorite


                // attachment

                adapterAttachment = AttachmentAdapter(
                    data = mutableListOf(),
                    onDelAtt = { item ->
                        if (item is AttachmentNoteEntity) {
                            noteViewModel.deleteAttachment(item)
                            listAttDel.add(item)
                            noteViewModel.getNoteWithDetails(idNote)
                        }
                    },
                    context = this
                )
                recyclerViewAttachment.adapter = adapterAttachment

                if (it.attachmentNotes.isNotEmpty()) {
                    recyclerViewAttachment.visibility = View.VISIBLE
                    adapterAttachment.updateDataChanged(it.attachmentNotes)
                } else {
                    recyclerViewAttachment.visibility = View.GONE
                }


                //task
                it.tasks.forEach { taskEtt ->
                    val task = Task(
                        idTask = taskEtt.idTask,
                        nameTask = taskEtt.nameTask,
                        isChecked = taskEtt.isChecked
                    )
                    tasks.add(task)

                }


                // recorder
                recorderAdapter = RecorderAdapter(
                    data = mutableListOf(),
                    onClickItem = {},
                    onDeleteRecorder = { recorderEtt ->
                        if (recorderEtt is AudioRecordEntity) {
                            noteViewModel.deleteAudio(recorderEtt)
                            listRecordAdd.add(recorderEtt)
                            noteViewModel.getNoteWithDetails(idNote)
                        }

                    }
                )

                recyclerViewAudio.adapter = recorderAdapter
                if (it.audioRecords.isNotEmpty()) {
                    recyclerViewAudio.visibility = View.VISIBLE
                    recorderAdapter.updateDataChanged(it.audioRecords.toMutableList())
                } else {
                    recyclerViewAudio.visibility = View.GONE

                }


//canvas

                canvasAdapter = CanvasAdapter(
                    data = mutableListOf(),
                    onDelete = { canvasEtt ->
                        if (canvasEtt is CustomCanvasEntity) {
                            noteViewModel.deleteCanvas(canvasEtt)
                            listCanvasDel.add(canvasEtt)
                            noteViewModel.getNoteWithDetails(idNote)
                        }
                    },
                    context = this
                )
                recyclerViewCanvas.adapter = canvasAdapter
                if (it.customCanvasList.isNotEmpty()) {
                    recyclerViewCanvas.visibility = View.VISIBLE
                    canvasAdapter.onUpdateDataChanged(it.customCanvasList.toMutableList())
                } else {
                    recyclerViewCanvas.visibility = View.GONE
                }


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


    private fun insertCheckBox() {
        if (!isTaskList) {
            binding.layoutAddTask.visibility = View.VISIBLE

            if (!::adapter.isInitialized) {  // Chỉ tạo Adapter 1 lần duy nhất
                recyclerView.layoutManager = LinearLayoutManager(this)
                adapter = TaskInAddNoteAdapter(
                    data = tasks.toMutableList(),
                    onDelNote = { task ->
                        tasks.remove(task)
                        adapter.updateDataChanged(tasks)
                        listTaskDel.add(task)
                    },
                    onChangeText = { s, task ->
                        tasks.find { it.idTask == task.idTask }?.nameTask = s
                        adapter.updateDataChanged(tasks)
                    },

                    onCheckChange = { isChecked, task ->
                        tasks.find { it.idTask == task.idTask }?.isChecked = isChecked
                        adapter.updateDataChanged(tasks)
                    }
                )
                recyclerView.adapter = adapter
            }

            binding.btnInsertTask.setOnClickListener {
                val newTask = Task(nameTask = "")
                tasks.add(newTask)
                adapter.updateDataChanged(tasks)
            }

        } else {
            binding.layoutAddTask.visibility = View.GONE
        }

        isTaskList = !isTaskList
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
        recyclerViewAttachment = binding.recyclerViewAttachment
        recyclerViewCanvas = binding.recyclerViewCanvas
        spannable = SpannableStringBuilder(editContent.text)
        recyclerView = binding.recyclerViewTask
        noteFontViewModel = ViewModelProvider(this)[NoteFontViewModel::class.java]
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        attachmentNoteViewModel = ViewModelProvider(this)[AttachmentNoteViewModel::class.java]
        recorderViewModel = ViewModelProvider(this)[RecorderViewModel::class.java]
        canvasViewModel = ViewModelProvider(this)[CanvasViewModel::class.java]
        recyclerViewAttachment.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        noteDao = DatabaseBuilder.getInstance(application).noteDao()
        repository = NoteRepository(noteDao)
        noteViewModel =
            ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]
        recyclerViewCanvas.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAudio = binding.recyclerViewAudio
        recyclerViewAudio.layoutManager = LinearLayoutManager(this)
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

    private val backPressedCallback = object : OnBackPressedCallback(enabled = true) {
        override fun handleOnBackPressed() {
            isEnabled = false
            onBackPressedDispatcher.onBackPressed()
            returnData()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                returnData()
                return true
            }

            R.id.note_alarm -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager =
                        ContextCompat.getSystemService(this, AlarmManager::class.java)
                    if (alarmManager?.canScheduleExactAlarms() == false) {
                        Intent().also { intent ->
                            intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                            this.startActivity(intent)
                        }
                    } else {
                        chooseCalender()
                    }
                } else {
                    chooseCalender()
                }
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

    private fun returnData() {
        listAttDel.forEach { noteViewModel.insertOrUpdateAttachment(it) }
        listAttAdd.forEach { noteViewModel.deleteAttachment(it) }
        listCanvasAdd.forEach { noteViewModel.deleteCanvas(it) }
        listCanvasDel.forEach { noteViewModel.insertCustomerCanvas(it) }
        listRecordAdd.forEach { noteViewModel.insertAudioRecorder(it) }
        listRecordDel.forEach { noteViewModel.deleteAudio(it) }
        finish()
    }

    private fun chooseCalender() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // Kiểm tra quyền trước
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+ cần xin quyền
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent) // Mở settings để cấp quyền
                return // Dừng hàm nếu chưa có quyền
            }
        }

        // Mở TimePicker
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(LocalTime.now().hour)
            .setMinute(LocalTime.now().minute)
            .setTitleText(getString(R.string.select_time))
            .build()
        timePicker.show(supportFragmentManager, "")

            timePicker.addOnPositiveButtonClickListener {
            val calender = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, timePicker.hour)
                set(java.util.Calendar.MINUTE, timePicker.minute)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }

            isReminder = true

            calendarAlarm = Calendar(
                timestampL = calender.timeInMillis,
                hour = calender.get(java.util.Calendar.HOUR_OF_DAY), // Lấy giờ đúng
                minute = calender.get(java.util.Calendar.MINUTE) // Lấy phút đúng
            )
        }

    }


    private fun initViewCanvas() {
        canvasViewModel.canvas.observe(this) {
            if (it.isEmpty()) {
                recyclerViewCanvas.visibility = View.GONE
            } else {
                recyclerViewCanvas.visibility = View.VISIBLE
                val adapter = CanvasAdapter(
                    data = it.toMutableList(),
                    onDelete = { canvas ->
                        if (canvas is CustomCanvas) {
                            canvasViewModel.removeCanvas(canvas)
                        }
                    },
                    context = this
                )
                recyclerViewCanvas.adapter = adapter


            }
        }
    }

    private fun saveNote() {
        if (editTitle.text.isNullOrBlank()) {
            Toast.makeText(this, "Title is not blank", Toast.LENGTH_SHORT).show()
            return
        }
        if (editContent.text.isNullOrBlank()) {
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
                isReminder = isReminder,
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
                isReminder = isReminder,
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


            if (isSaveOrAdd) {
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


            }


            //task list

            this.tasks.let { tasks ->
                tasks.forEach { task ->
                    val taskEntity = TaskEntity(
                        idTask = task.idTask,
                        noteId = idNote,
                        nameTask = task.nameTask,
                        isChecked = task.isChecked
                    )
                    noteViewModel.addTaskEntity(taskEntity)
                }

            }
            this.listTaskDel.forEach { taskDel ->
                val taskEtt = TaskEntity(
                    idTask = taskDel.idTask,
                    noteId = idNote,
                    nameTask = taskDel.nameTask,
                    isChecked = taskDel.isChecked
                )
                noteViewModel.deleteTask(taskEtt)
            }

            if (isSaveOrAdd) {
                recorderViewModel.recorders.observe(this) { recorder ->

                    recorder?.forEach { itemRecorder ->
                        val audioRecordEntity = AudioRecordEntity(
                            idAudio = itemRecorder.id,
                            noteId = idNote,
                            fileName = itemRecorder.fileName,
                            uri = itemRecorder.uri,
                            isPlaying = itemRecorder.isPlaying
                        )
                        listRecorder.add(audioRecordEntity)

                    }

                    // them recorder
                    if (listRecorder.isNotEmpty()) {
                        listRecorder.forEach { recorder ->
                            noteViewModel.insertAudioRecorder(recorder)
                        }
                    }

                }
            }
            // alarm

            calendarAlarm?.let {
                setAlarm(it.timestampL, idNote)
            }


            if (isSaveOrAdd) {
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

    private fun setAlarm(timestampL: Long, requestCode: Long) {

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(
            this, AlarmReceiver::class.java
        )

        intent.putExtra("id_note", this.idNote)
        intent.putExtra("title_note", editTitle.text.toString())
        intent.putExtra("content_note", editContent.text.toString())

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )



        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, timestampL, pendingIntent
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
            val soundUri = Uri.parse("android.resource://${packageName}/raw/mat_ket_noi_remix")

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM) // Đảm bảo hoạt động như âm báo thức
                .build()

            channel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
    }


}