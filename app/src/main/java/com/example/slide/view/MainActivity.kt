package com.example.slide.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slide.R
import com.example.slide.action.SlideAction
import com.example.slide.adapter.SlideListAdapter
import com.example.slide.databinding.ActivityMainBinding
import com.example.slide.factory.SlideViewModelFactory
import com.example.slide.listener.SlideListItemClickListener
import com.example.slide.listener.SlideMoveCallListener
import com.example.slide.listener.SlideViewListener
import com.example.slide.manager.SlideManager
import com.example.slide.model.ImageSlide
import com.example.slide.model.Slide
import com.example.slide.model.SlideViewModel
import com.example.slide.model.SquareSlide
import com.example.slide.util.SlideColorUtil

class MainActivity : AppCompatActivity(), SlideListItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SlideViewModel
    private lateinit var slideAdapter: SlideListAdapter
    private lateinit var getImageResultLauncher: ActivityResultLauncher<Intent>
    private val whiteColor = 255
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initListeners()
        initRecyclerView()
        initViewModel()

        getImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedImageUri = result.data?.data
                    val selectedSlide = viewModel.selectedSlide.value

                    if (selectedSlide is ImageSlide) {
                        val imageBytes = selectedImageUri?.let { uriToByteArray(it) }
                        imageBytes?.let {
                            selectedSlide.imageBytes = it
                            viewModel.changeImgByteArr(selectedSlide, it)
                        }
                    }

                }
            }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            SlideViewModelFactory(SlideManager())
        )[SlideViewModel::class.java]
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

        viewModel.selectedSlideIndex.observe(this, Observer { index ->
            slideAdapter.slideClickItemUpdate(index!!)
        })
    }

    private fun updateSlideProperties(slide: Slide?) {
        slide?.let {
            if (it is SquareSlide) {
                binding.incRightMenu.etBackground.setBackgroundColor(it.backgroundColor)
                val hex = Integer.toHexString(it.backgroundColor)
                binding.incRightMenu.etBackground.text = "0x${hex.uppercase()}"
            }

            binding.incRightMenu.tvAlpha.text = it.alpha.toString()
        }
    }

    private fun initListeners() {
        binding.boardView.setOnClickListener {
            viewModel.clearSelectedSlide()
            binding.incRightMenu.etBackground.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            binding.incRightMenu.etBackground.text = ""
            binding.incRightMenu.tvAlpha.text = ""
        }

        binding.incRightMenu.etBackground.setOnClickListener {
            val color = SlideColorUtil.generateRandomColor()
            val selectedSlideId = viewModel.selectedSlideId.value

            selectedSlideId?.let {
                updateBackgroundColor(color)
            }
        }

        binding.incRightMenu.tvPlus.setOnClickListener {
            updateAlpha(1)
        }

        binding.incRightMenu.tvMinus.setOnClickListener {
            updateAlpha(-1)
        }

        binding.incSlideList.btnAddSlide.setOnClickListener {
            viewModel.processAction(SlideAction.AddSlide)
            val newSlide = viewModel.slides.value?.last()
            newSlide?.let { makeSlideView(it) }
        }
    }

    private fun makeSlideView(newSlide: Slide) {
        val slideView = SlideView(this, null, newSlide.id, viewModel)

        if (newSlide is ImageSlide) {
            slideView.slideViewListener = object : SlideViewListener {
                override fun onSlideDoubleClicked(svId: String) {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    getImageResultLauncher.launch(pickPhoto)
                }
            }
        }

        binding.boardView.addView(slideView)
    }

    private fun initRecyclerView() {
        slideAdapter = SlideListAdapter(mutableListOf(), this)
        binding.incSlideList.rvSlide.layoutManager = LinearLayoutManager(this)
        binding.incSlideList.rvSlide.adapter = slideAdapter

        val callback = SlideMoveCallListener(slideAdapter)
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

    private fun updateBackgroundColor(color: Int) {
        val slide = viewModel.selectedSlide.value

        if (slide is SquareSlide) {
            viewModel.processAction(SlideAction.ChangeColor(color))
            binding.incRightMenu.etBackground.setBackgroundColor(color)
            val hex = Integer.toHexString(color)
            binding.incRightMenu.etBackground.text = "0x${hex.uppercase()}"
        } else {
            if (slide is ImageSlide) {
                binding.incRightMenu.etBackground.setBackgroundColor(whiteColor)
                val hex = Integer.toHexString(color)
                binding.incRightMenu.etBackground.text = ""
            }
        }
    }

    private fun updateAlpha(value: Int) {
        val selectedSlide = viewModel.selectedSlide.value

        selectedSlide?.let {
            viewModel.processAction(SlideAction.ChangeAlpha(selectedSlide.alpha + value))
        }
    }

    private fun uriToByteArray(uri: Uri): ByteArray {
        val inputStream = contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: byteArrayOf()
    }

}