package com.example.slide.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.slide.R
import com.example.slide.action.SlideAction
import com.example.slide.model.SlideViewModel
import com.example.slide.model.SquareSlide

class SquareBaseSlideView(
    context: Context,
    svId: String,
    viewModel: SlideViewModel
) : BaseSlideView(context, svId, viewModel) {

    override fun setupLayoutParams() {
        val slide = getSlide()!!
        val sizeInPixels = dpToPx(DEFAULT_DP_SIZE)
        val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)

        if (slide.lastPosition.first == 0f && slide.lastPosition.second == 0f) {
            layoutParams.startToStart = R.id.board_view
            layoutParams.endToEnd = R.id.board_view
            layoutParams.topToTop = R.id.board_view
            layoutParams.bottomToBottom = R.id.board_view
        } else {
            this.x = slide.lastPosition.first
            this.y = slide.lastPosition.second
        }

        layoutParams.height = slide!!.sideLength
        layoutParams.width = slide.sideLength

        this.layoutParams = layoutParams
    }

    override fun handleTouch(view: View, motionEvent: MotionEvent) {
        handleMovement(view, motionEvent)
    }

    override fun drawSlide(slide: Any, canvas: Canvas?) {
        val squareSlide = slide as SquareSlide

        val alpha = (squareSlide.alpha * 10 * 255) / 100
        val color = squareSlide.backgroundColor

        fillPaint.color = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), strokePaint)
    }
}