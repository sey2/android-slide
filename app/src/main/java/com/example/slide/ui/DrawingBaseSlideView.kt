package com.example.slide.ui

import DrawingSlide
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.slide.R
import com.example.slide.action.SlideAction
import com.example.slide.model.SlideViewModel
import kotlin.math.max
import kotlin.math.min

class DrawingBaseSlideView(
    context: Context,
    svId: String,
    viewModel: SlideViewModel
) : BaseSlideView(context, svId, viewModel) {

    override fun setupLayoutParams() {
        val sizeInPixels = dpToPx(DEFAULT_DP_SIZE)
        val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)

        layoutParams.startToStart = R.id.board_view
        layoutParams.endToEnd = R.id.board_view
        layoutParams.topToTop = R.id.board_view
        layoutParams.bottomToBottom = R.id.board_view

        layoutParams.width = 0
        layoutParams.height = 0
        this.layoutParams = layoutParams
    }


    override fun handleTouch(view: View, motionEvent: MotionEvent) {
        val slide = getSlide()
        if (slide is DrawingSlide && slide.isEditable) { // Drawable Slide이며 처음 추가 되었을때만 그리기 가능
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(slide.isEditable) {
                        slide.path.moveTo(motionEvent.x, motionEvent.y)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if(slide.isEditable) {
                        slide.path.lineTo(motionEvent.x, motionEvent.y)
                        slide.minX = min(slide.minX, motionEvent.x)
                        slide.minY = min(slide.minY, motionEvent.y)
                        slide.maxX = max(slide.maxX, motionEvent.x)
                        slide.maxY = max(slide.maxY, motionEvent.y)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    slide.isEditable = false
                    invalidate()
                    viewModel.processAction(SlideAction.SelectSlide(svId))
                }
            }
        }else { // Drawable Slide 객체이면 그려진 이후에 이동 가능
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
                }
            }
        }
    }

    override fun drawSlide(slide: Any, canvas: Canvas?) {
        val drawingSlide = slide as DrawingSlide
        drawingSlide.paint.style = Paint.Style.STROKE
        drawingSlide.paint.strokeWidth = 5f
        drawingSlide.paint.isAntiAlias = true
        canvas?.drawPath(slide.path, slide.paint)

        if(!drawingSlide.isEditable) {
            strokePaint.color = ContextCompat.getColor(context, R.color.red)
            strokePaint.strokeWidth = 5f
            canvas?.drawRect(slide.minX, slide.minY, slide.maxX, slide.maxY, strokePaint)
        }
    }
}