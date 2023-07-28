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
import com.example.slide.drawing.PathOperation
import kotlin.math.max
import kotlin.math.min

class DrawingBaseSlideView(
    context: Context,
    svId: String,
    viewModel: SlideViewModel
) : BaseSlideView(context, svId, viewModel) {

    private var lastTouchX: Float = 0.0f
    private var lastTouchY: Float = 0.0f
    override fun setupLayoutParams() {
        val sizeInPixels = dpToPx(DEFAULT_DP_SIZE)
        val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)
        val slide = getSlide()!!

        if (slide.lastPosition.first == 0f && slide.lastPosition.second == 0f) {
            layoutParams.startToStart = R.id.board_view
            layoutParams.endToEnd = R.id.board_view
            layoutParams.topToTop = R.id.board_view
            layoutParams.bottomToBottom = R.id.board_view
        } else {
            this.x = slide.lastPosition.first
            this.y = slide.lastPosition.second
        }

        layoutParams.width = 0
        layoutParams.height = 0
        this.layoutParams = layoutParams
    }


    override fun handleTouch(view: View, motionEvent: MotionEvent) {
        val slide = getSlide() as DrawingSlide
        if (slide.isEditable) { // 처음 추가 되었을때만 그리기 가능
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(slide.isEditable) {
                        slide.path.moveTo(motionEvent.x, motionEvent.y)
                        lastTouchX = motionEvent.x
                        lastTouchY = motionEvent.y
                        slide.pathData.operations.add(PathOperation.MoveTo(motionEvent.x, motionEvent.y))
                        invalidate()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if(slide.isEditable) {
                        val endX = (lastTouchX + motionEvent.x) / 2
                        val endY = (lastTouchY + motionEvent.y) / 2
                        slide.path.quadTo(lastTouchX, lastTouchY, endX, endY)
                        slide.pathData.operations.add(PathOperation.QuadTo(lastTouchX, lastTouchY, endX, endY))
                        lastTouchX = motionEvent.x
                        lastTouchY = motionEvent.y
                        slide.minX = min(slide.minX, motionEvent.x)
                        slide.minY = min(slide.minY, motionEvent.y)
                        slide.maxX = max(slide.maxX, motionEvent.x)
                        slide.maxY = max(slide.maxY, motionEvent.y)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    slide.isEditable = false
                    slide.lastPosition = Pair(view.x, view.y)
                    invalidate()
                    viewModel.processAction(SlideAction.SelectSlide(svId))
                }
            }
        }else { // Drawable Slide 객체이면 그려진 이후에 이동 가능
            handleMovement(view, motionEvent)
        }
    }

    override fun drawSlide(slide: Any, canvas: Canvas?) {
        val drawingSlide = slide as DrawingSlide
        drawingSlide.paint.style = Paint.Style.STROKE
        drawingSlide.paint.strokeWidth = DEFAULT_STROKE_WIDTH
        drawingSlide.paint.isAntiAlias = true
        drawingSlide.paint.strokeCap = Paint.Cap.ROUND
        drawingSlide.paint.color = drawingSlide.backgroundColor!!
        canvas?.drawPath(slide.path, slide.paint)

        if(!drawingSlide.isEditable) {
            strokePaint.color = ContextCompat.getColor(context, R.color.red)
            strokePaint.strokeWidth = DEFAULT_STROKE_WIDTH
            canvas?.drawRect(slide.minX, slide.minY, slide.maxX, slide.maxY, strokePaint)
        }
    }
}