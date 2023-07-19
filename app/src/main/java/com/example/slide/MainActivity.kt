package com.example.slide

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slide.adapter.SlideListAdapter
import com.example.slide.databinding.ActivityMainBinding
import com.example.slide.factory.SlideViewModelFactory
import com.example.slide.listener.OnSlideListItemListener
import com.example.slide.listener.SlideMoveCallback
import com.example.slide.model.Slide
import com.example.slide.model.SlideViewModel
import com.example.slide.util.ColorUtil
import com.example.slide.view.SlideView

class MainActivity : AppCompatActivity(), OnSlideListItemListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SlideViewModel
    private lateinit var slideAdapter: SlideListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViewModel()
        initListeners()
        initRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        viewModel = ViewModelProvider(this, SlideViewModelFactory(SlideManager()))[SlideViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.selectedSlide.observe(this, Observer { slide ->
            slide?.let {
                updateSlideProperties(slide)
            }
        })

        viewModel.slides.observe(this, Observer { slides ->
            slides?.let {
                slideAdapter.setItems(it)
                updateSlideProperties(viewModel.selectedSlide.value)
            }
        })
    }

    private fun updateSlideProperties(slide: Slide?) {
        slide?.let {
            binding.incRightMenu.etBackground.setBackgroundColor(it.backgroundColor)
            val hex = Integer.toHexString(it.backgroundColor)
            binding.incRightMenu.etBackground.text = "0x${hex.uppercase()}"
            binding.incRightMenu.tvAlpha.text = it.alpha.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        binding.boardView.setOnClickListener {
            viewModel.clearSelectedSlide()
            binding.incRightMenu.etBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.incRightMenu.etBackground.text = ""
            binding.incRightMenu.tvAlpha.text = ""
        }

        binding.incRightMenu.etBackground.setOnClickListener {
            val color = ColorUtil.generateRandomColor()
            val selectedSlideId = viewModel.selectedSlideId.value

            selectedSlideId?.let {
                val slideView: SlideView = binding.boardView.findViewWithTag(selectedSlideId)
                updateBackgroundColor(slideView, color)
            }
        }

        binding.incRightMenu.tvPlus.setOnClickListener {
            updateAlpha(1)
        }

        binding.incRightMenu.tvMinus.setOnClickListener {
            updateAlpha(-1)
        }

        binding.incSlideList.btnAddSlide.setOnClickListener {
            viewModel.addSlide()
            val newSlide = viewModel.slides.value?.last()
            newSlide?.let { makeSlideView(it) }
        }
    }

    private fun makeSlideView(newSlide: Slide) {
        val slideView = SlideView(this, null, newSlide.id, viewModel)
        slideView.changeColor(newSlide.backgroundColor, newSlide.alpha * 20)
        binding.boardView.addView(slideView)
    }

    private fun initRecyclerView() {
        slideAdapter = SlideListAdapter(mutableListOf(), this)
        binding.incSlideList.rvSlide.layoutManager = LinearLayoutManager(this)
        binding.incSlideList.rvSlide.adapter = slideAdapter

        val callback = SlideMoveCallback(slideAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.incSlideList.rvSlide)
    }

    override fun onItemClick(slideIndex: Int, holder: SlideListAdapter.SlideListViewHolder) {
        viewModel.setSlides(slideAdapter.getAllItems())
        val selectedId = slideAdapter.getAllItems()[slideIndex].id

        if (viewModel.selectedSlideId.value == selectedId) {
            viewModel.clearSelectedSlide()
        } else {
            viewModel.selectSlide(slideIndex)
            viewModel.selectSlide(selectedId)
        }
    }

    private fun updateBackgroundColor(slideView: SlideView, color: Int) {
        viewModel.changeColor(color)
        val slide = viewModel.selectedSlide.value
        slideView?.changeColor(slide!!.backgroundColor, slide.alpha * 20)
        binding.incRightMenu.etBackground.setBackgroundColor(color)
        val hex = Integer.toHexString(color)
        binding.incRightMenu.etBackground.text = "0x${hex.uppercase()}"
    }

    private fun updateAlpha(value: Int) {
        val selectedSlide = viewModel.selectedSlide.value
        val selectedSlideId = viewModel.selectedSlideId.value

        selectedSlide?.let {
            viewModel.changeAlpha(selectedSlide.alpha + value)
        }

        selectedSlideId?.let {
            val slideView: SlideView = binding.boardView.findViewWithTag(selectedSlideId)
            slideView.changeColor(selectedSlide!!.backgroundColor, selectedSlide!!.alpha * 10)
        }
    }

}