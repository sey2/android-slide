package com.example.slide.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.slide.R
import com.example.slide.action.SlideAction
import com.example.slide.listener.SlideViewListener
import com.example.slide.model.ImageSlide
import com.example.slide.model.SlideViewModel
import com.example.slide.model.SquareSlide

@SuppressLint("ClickableViewAccessibility")
class SlideView(
    context: Context,
    attrs: AttributeSet?,
    val svId: String,
    private val viewModel: SlideViewModel
) : View(context, attrs) {
    companion object {
        private const val DEFAULT_DP_SIZE = 150
        private const val DOUBLE_CLICK_TIME_THRESHOLD = 200
    }

    private val selectedStrokeWidth: Float = resources.getDimension(R.dimen.selected_stroke_width)
    private val selectedStrokeColor: Int = ContextCompat.getColor(context, R.color.red)
    private val defaultStrokeWidth: Float = resources.getDimension(R.dimen.default_stroke_width)
    private val defaultStrokeColor: Int = ContextCompat.getColor(context, R.color.transparent)
    private var bitmap: Bitmap? = createDefaultImageBitmap()
    private val imagePaint: Paint = createPaint()
    private val strokePaint: Paint =
        createPaint(strokeWidth = defaultStrokeWidth, color = defaultStrokeColor)
    private val fillPaint: Paint = createPaint(style = Paint.Style.FILL, color = defaultStrokeColor)

    private var dX = 0f
    private var dY = 0f
    private var lastClickTime = 0L
    var slideViewListener: SlideViewListener? = null

    init {
        setupView()
        setupObservers()
        setupOnTouchListener()
        setupLayoutParams()
    }
    private fun createPaint(
        style: Paint.Style = Paint.Style.STROKE,
        strokeWidth: Float = 0f,
        color: Int = 0
    ): Paint {
        return Paint().apply {
            isAntiAlias = true
            this.style = style
            this.strokeWidth = strokeWidth
            this.color = color
        }
    }

    private fun setupView() {
        isClickable = true
        isFocusable = true
        tag = svId
    }

    private fun setupObservers() {
        viewModel.selectedSlideId.observe((context as LifecycleOwner), Observer { selectedSlideId ->
            isSelected = svId == selectedSlideId
            viewModel.processAction(SlideAction.ChangeSelectedIndex)
            invalidate()
        })

        viewModel.slides.observe((context as LifecycleOwner), Observer { slides ->
            updateSlide()
        })
    }

    private fun updateSlide() {
        getSlide()?.let { slide ->
            if (slide is ImageSlide && slide.imageBytes != null) {
                bitmap = BitmapFactory.decodeByteArray(
                    slide.imageBytes,
                    0,
                    slide.imageBytes!!.size
                )
                setupLayoutParams()
            }
            invalidate()
        }
    }

    private fun setupOnTouchListener() {
        setOnTouchListener { view, motionEvent ->
            handleTouch(view, motionEvent)
            true
        }
    }

    private fun setupLayoutParams() {
        val sizeInPixels = dpToPx(DEFAULT_DP_SIZE)
        val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)

        layoutParams.startToStart = R.id.board_view
        layoutParams.endToEnd = R.id.board_view
        layoutParams.topToTop = R.id.board_view
        layoutParams.bottomToBottom = R.id.board_view

        val slide = getSlide()

        if(slide is ImageSlide){
            bitmap?.let {
                val scale = context.resources.displayMetrics.density
                val padding = dpToPx(10)
                layoutParams.height = ((it.height * scale).toInt() - 2 * padding)
                layoutParams.width = ((it.width * scale).toInt() - 2 * padding)
            }
        } else if(slide is SquareSlide){
            layoutParams.height = slide.sideLength
            layoutParams.width = slide.sideLength
        }


        this.layoutParams = layoutParams
    }

    private fun handleTouch(view: View, motionEvent: MotionEvent) {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - motionEvent.rawX
                dY = view.y - motionEvent.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                view.animate()
                    .x(motionEvent.rawX + dX)
                    .y(motionEvent.rawY + dY)
                    .setDuration(0)
                    .start()
            }

            MotionEvent.ACTION_UP -> {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_THRESHOLD) { // Double click
                    slideViewListener?.onSlideDoubleClicked(svId)
                } else { // Single click
                    isSelected = !isSelected
                    viewModel.selectSlide(svId)
                }
                lastClickTime = clickTime
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (isSelected) {
            strokePaint.strokeWidth = selectedStrokeWidth
            strokePaint.color = selectedStrokeColor
        } else {
            strokePaint.strokeWidth = defaultStrokeWidth
            strokePaint.color = defaultStrokeColor
        }

        val slide = getSlide()
        when (slide) {
            is ImageSlide -> drawImageSlide(slide, canvas)
            is SquareSlide -> drawSquareSlide(slide, canvas)
        }
    }

    private fun getSlide() = viewModel.slides.value?.find { it.id == svId }

    private fun drawImageSlide(slide: ImageSlide, canvas: Canvas?) {
        bitmap?.let {
            val padding = dpToPx(10) // padding of 10 dp
            val maxDrawableWidth = width.toFloat() - 2 * padding
            val maxDrawableHeight = height.toFloat() - 2 * padding

            val bitmapWidth = it.width.toFloat()
            val bitmapHeight = it.height.toFloat()

            val scale: Float = if (bitmapWidth / bitmapHeight > maxDrawableWidth / maxDrawableHeight) {
                maxDrawableWidth / bitmapWidth
            } else {
                maxDrawableHeight / bitmapHeight
            }

            val outWidth = scale * bitmapWidth
            val outHeight = scale * bitmapHeight
            val left = (width - outWidth) / 2
            val top = (height - outHeight) / 2

            imagePaint.alpha = (slide.alpha * 10 * 255) / 100

            val matrix = Matrix()
            matrix.postScale(scale, scale)
            matrix.postTranslate(left, top)

            canvas?.drawBitmap(it, matrix, imagePaint)

            // Increase the border size by 10dp
            val borderRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            canvas?.drawRect(borderRect, strokePaint)
        }
    }

    private fun drawSquareSlide(slide: SquareSlide, canvas: Canvas?) {
        val alpha = (slide.alpha * 10 * 255) / 100
        val color = slide.backgroundColor

        fillPaint.color = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), strokePaint)
    }

    private fun createDefaultImageBitmap(): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, R.drawable.img_default)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val intrinsicWidth = drawable!!.intrinsicWidth
        val intrinsicHeight = drawable.intrinsicHeight

        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return null
        }

        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun dpToPx(dp: Int = DEFAULT_DP_SIZE): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

}