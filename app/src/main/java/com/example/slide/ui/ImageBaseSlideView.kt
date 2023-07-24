package com.example.slide.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.slide.R
import com.example.slide.action.SlideAction
import com.example.slide.model.ImageSlide
import com.example.slide.model.SlideViewModel

class ImageBaseSlideView(
    context: Context,
    svId: String,
    viewModel: SlideViewModel
) : BaseSlideView(context, svId, viewModel) {
    private var bitmap: Bitmap? = createDefaultImageBitmap()

    override fun setupLayoutParams() {
        val sizeInPixels = dpToPx(DEFAULT_DP_SIZE)
        val layoutParams = ConstraintLayout.LayoutParams(sizeInPixels, sizeInPixels)

        layoutParams.startToStart = R.id.board_view
        layoutParams.endToEnd = R.id.board_view
        layoutParams.topToTop = R.id.board_view
        layoutParams.bottomToBottom = R.id.board_view

        bitmap?.let {
            val scale = context.resources.displayMetrics.density
            val padding = dpToPx(10)
            layoutParams.height = ((it.height * scale).toInt() - 2 * padding)
            layoutParams.width = ((it.width * scale).toInt() - 2 * padding)
        }

        this.layoutParams = layoutParams
    }

    override fun handleTouch(view: View, motionEvent: MotionEvent) {
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
                if (clickTime - lastClickTime < BaseSlideView.DOUBLE_CLICK_TIME_THRESHOLD) { // Double click
                    slideViewListener?.onSlideDoubleClicked(svId)
                } else { // Single click
                    isSelected = !isSelected
                    viewModel.processAction(SlideAction.SelectSlide(svId))
                }
                lastClickTime = clickTime
            }
        }
    }

    override fun drawSlide(slide: Any, canvas: Canvas?) {
        val imageSlide = slide as ImageSlide
        bitmap?.let {
            val padding = dpToPx(10)
            val maxDrawableWidth = width.toFloat() - 2 * padding
            val maxDrawableHeight = height.toFloat() - 2 * padding

            val bitmapWidth = it.width.toFloat()
            val bitmapHeight = it.height.toFloat()

            val scale: Float =
                if (bitmapWidth / bitmapHeight > maxDrawableWidth / maxDrawableHeight) {
                    maxDrawableWidth / bitmapWidth
                } else {
                    maxDrawableHeight / bitmapHeight
                }

            val outWidth = scale * bitmapWidth
            val outHeight = scale * bitmapHeight
            val left = (width - outWidth) / 2
            val top = (height - outHeight) / 2

            imagePaint.alpha = (imageSlide.alpha * 10 * 255) / 100

            val matrix = Matrix()
            matrix.postScale(scale, scale)
            matrix.postTranslate(left, top)

            canvas?.drawBitmap(it, matrix, imagePaint)

            val borderRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            canvas?.drawRect(borderRect, strokePaint)
        }
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

    override fun updateSlide() {
        getSlide()?.let { slide ->
            if (slide is ImageSlide && slide.imageBytes != null) {
                bitmap = BitmapFactory.decodeByteArray(
                    slide.imageBytes,
                    0,
                    slide.imageBytes!!.size
                )
                setupLayoutParams()
            }

            super.updateSlide()
        }
    }
}