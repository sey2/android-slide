package com.example.slide.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.slide.R
import com.example.slide.action.SlideAction
import com.example.slide.listener.SlideViewListener
import com.example.slide.model.SlideViewModel

abstract class BaseSlideView(
    context: Context,
    val svId: String,
    protected val viewModel: SlideViewModel
) : View(context) {

    companion object {
        const val DEFAULT_DP_SIZE = 150
        const val DOUBLE_CLICK_TIME_THRESHOLD = 200
        const val DEFAULT_STROKE_WIDTH = 5f
    }

    private val selectedStrokeWidth: Float = resources.getDimension(R.dimen.selected_stroke_width)
    private val selectedStrokeColor: Int = ContextCompat.getColor(context, R.color.red)
    private val defaultStrokeWidth: Float = resources.getDimension(R.dimen.default_stroke_width)
    private val defaultStrokeColor: Int = ContextCompat.getColor(context, R.color.transparent)
    protected val imagePaint: Paint = createPaint()
    protected val strokePaint: Paint =
        createPaint(strokeWidth = defaultStrokeWidth, color = defaultStrokeColor)
    protected val fillPaint: Paint =
        createPaint(style = Paint.Style.FILL, color = defaultStrokeColor)

    private var dX = 0f
    private var dY = 0f
    private var lastClickTime = 0L
    var slideViewListener: SlideViewListener? = null

    protected fun getSlide() = viewModel.slides.value?.find { it.id == svId }

    protected open fun updateSlide() {
        invalidate()
    }

    protected abstract fun handleTouch(view: View, motionEvent: MotionEvent)

    protected abstract fun drawSlide(slide: Any, canvas: Canvas?)

    abstract fun setupLayoutParams()

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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupOnTouchListener() {
        setOnTouchListener { view, motionEvent ->
            handleTouch(view, motionEvent)
            true
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

        getSlide()?.let { drawSlide(it, canvas) }
    }

    protected open fun handleMovement(view: View, motionEvent: MotionEvent) {
        val slide = getSlide()
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
                    viewModel.processAction(SlideAction.SelectSlide(svId))
                }
                lastClickTime = clickTime
                slide!!.lastPosition = Pair(view.x, view.y)
            }
        }
    }

    protected fun dpToPx(dp: Int = DEFAULT_DP_SIZE): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
