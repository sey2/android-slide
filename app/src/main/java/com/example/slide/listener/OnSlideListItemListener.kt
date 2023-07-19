package com.example.slide.listener

import com.example.slide.adapter.SlideListAdapter

interface OnSlideListItemListener {
    fun onItemClick(slideIndex: Int, holder: SlideListAdapter.SlideListViewHolder)
}