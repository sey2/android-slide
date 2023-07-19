package com.example.slide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.slide.R
import com.example.slide.databinding.SlideItemBinding
import com.example.slide.listener.OnSlideListItemListener
import com.example.slide.model.Slide

class SlideListAdapter(
    private val slideList: List<Slide>,
    val onSlideClickListener: OnSlideListItemListener
) :
    RecyclerView.Adapter<SlideListAdapter.SlideListViewHolder>() {
    private var itemList: MutableList<Slide> = slideList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideListViewHolder {
        val binding =
            DataBindingUtil.inflate<SlideItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.slide_item,
                parent,
                false
            )

        return SlideListViewHolder(binding)
    }

    fun getAllItems() = itemList

    fun setItems(slides: List<Slide>) {
        itemList = slides.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SlideListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class SlideListViewHolder(private val binding: SlideItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemRoot.setOnClickListener {
                onSlideClickListener.onItemClick(adapterPosition, this)
            }
        }

        fun bind(position: Int) {
            binding.tvNum.text = (position + 1).toString()
        }
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val slide = itemList[fromPosition]
        itemList.removeAt(fromPosition)
        itemList.add(toPosition, slide)

        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
    }
}