package com.example.slide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.slide.R
import com.example.slide.databinding.SlideItemBinding
import com.example.slide.listener.SlideListItemClickListener
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide

class SlideListAdapter(
    var itemList: MutableList<Slide>,
    private val onSlideClickListener: SlideListItemClickListener
) :
    RecyclerView.Adapter<SlideListAdapter.SlideListViewHolder>() {
    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideListViewHolder {
        val binding =
            DataBindingUtil.inflate<SlideItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.slide_item,
                parent,
                false
            )

        return SlideListViewHolder(binding).apply {
            itemView.setOnClickListener {
                updatePosition(adapterPosition)
                onSlideClickListener.onItemClick(adapterPosition, this)
            }
        }
    }

    fun addItem(item: Slide) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    fun getAllItems() = itemList

    fun clearItem() {
        itemList.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SlideListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun updatePosition(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(selectedPosition)
    }


    inner class SlideListViewHolder(private val binding: SlideItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemRoot.setOnLongClickListener { view ->
                PopupMenu(view.context, view).apply {
                    menuInflater.inflate(R.menu.context_menu, menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menu_to_back -> moveItemAndUpdate(
                                adapterPosition,
                                itemList.size - 1
                            )

                            R.id.menu_back -> if (adapterPosition < itemList.size - 1) moveItemAndUpdate(
                                adapterPosition,
                                adapterPosition + 1
                            )

                            R.id.menu_forward -> if (adapterPosition > 0) moveItemAndUpdate(
                                adapterPosition,
                                adapterPosition - 1
                            )

                            R.id.menu_to_front -> moveItemAndUpdate(adapterPosition, 0)
                        }
                        true
                    }
                }.show()
                true
            }
        }

        fun bind(position: Int) {
            binding.tvNum.text = (position + 1).toString()

            if (itemList[position] is ImageSlide) {
                binding.ivItem.setImageResource(R.drawable.img_slide)
            } else {
                binding.ivItem.setImageResource(R.drawable.img_squar)
            }

            binding.isSelected = position == selectedPosition
            binding.executePendingBindings()
        }
    }

    fun slideClickItemUpdate(newPosition: Int) {
        val prevSelectIdx = selectedPosition
        selectedPosition = newPosition
        notifyItemChanged(prevSelectIdx)
        notifyItemChanged(newPosition)
    }

    fun moveItemAndUpdate(fromPosition: Int, toPosition: Int) {
        val slide = itemList[fromPosition]
        itemList.removeAt(fromPosition)
        itemList.add(toPosition, slide)

        notifyItemMoved(fromPosition, toPosition)
        updatePosition(toPosition)
    }
}