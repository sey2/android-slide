package com.example.slide.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class SlideInLeftAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.translationX = holder.itemView.width.toFloat()
        holder.itemView.animate().translationX(0f).setDuration(500).setListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                dispatchAddFinished(holder)
            }
        }).start()
        return true
    }
}
