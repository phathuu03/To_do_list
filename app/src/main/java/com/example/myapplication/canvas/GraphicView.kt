package com.example.myapplication.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.myapplication.model.CustomCanvas
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GraphicView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var paint = Paint()

    private var path = Path()

    private var bitmap: Bitmap? = null

    private var canvasBitmap: Canvas? = null

    private var uri: Uri? = null

    private var fileName: String? = null


    init {
        paint.color = Color.BLACK          // Màu đen
        paint.style = Paint.Style.STROKE   // Chỉ vẽ viền, không tô màu bên trong
        paint.strokeWidth = 8f             // Độ dày nét vẽ
        paint.isAntiAlias = true           // Làm mịn nét vẽ
        paint.strokeJoin = Paint.Join.ROUND // Góc bo tròn
        paint.strokeCap = Paint.Cap.ROUND
        Log.d("color path", paint.color.toString())

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap!!)
        canvasBitmap?.drawColor(Color.WHITE)
    }

    fun setColorPaint(color: Int) {
        this.paint.color = color
        invalidate()
        Log.d("color path", paint.color.toString())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint) // Vẽ nội dung đã lưu trên Bitmap
        canvas.drawPath(path, paint) // Vẽ đường vẽ hiện tại
    }


    // Xử lý sự kiện chạm màn hình để vẽ
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y) // Khi chạm xuống, bắt đầu vẽ từ điểm đó
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y) // Khi di chuyển ngón tay, vẽ đường theo điểm chạm
                canvasBitmap?.drawPath(path, paint) // Vẽ trực tiếp lên Bitmap
            }

            MotionEvent.ACTION_UP -> {
                path.reset() // Sau khi nhấc tay, đặt lại đường vẽ để không vẽ đè lên
            }
        }
        invalidate() // Gọi lại onDraw() để cập nhật View
        return true
    }

    // Hàm xóa canvas (vẽ lại toàn bộ màn hình với nền trắng)
    fun clearCanvas() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap!!)
        canvasBitmap?.drawColor(Color.WHITE) // Đặt lại màu nền
        invalidate() // Cập nhật lại giao diện
    }

    // Trả về ảnh Bitmap chứa hình vẽ
    private fun getBitmap(): Bitmap {
        return bitmap!!
    }

    fun saveDrawing(context: Context): String? {
        val bitmap = getBitmap() // Lấy bitmap hiện tại từ canvas

        return saveBitmapToPrivateStorage(context, bitmap)
    }

    fun getCanvas(): CustomCanvas?{

        fileName?.let { name ->
            val file = File(context.getExternalFilesDir(null), name) // ✅ Lấy file từ thư mục đúng
            if (file.exists()) {
                val uri = Uri.fromFile(file)
                return CustomCanvas(fileName = name, uri = uri)
            }
        }


        return null
    }




    private fun saveBitmapToPrivateStorage(context: Context, bitmap: Bitmap): String? {
        val directory = context.getExternalFilesDir(null) // Thư mục private của app trên bộ nhớ ngoài
        directory?.let {
            fileName = "drawing_${System.currentTimeMillis()}.png"

            val file = fileName?.let { it1 -> File(directory, it1) }

            return try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // Lưu ảnh dưới dạng PNG
                }
                file?.name
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        return null
    }

}