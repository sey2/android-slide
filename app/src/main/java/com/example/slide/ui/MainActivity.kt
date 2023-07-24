package com.example.slide.ui

import DrawingSlide
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slide.R
import com.example.slide.action.SlideAction
import com.example.slide.adapter.SlideInLeftAnimator
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
import java.io.OutputStream

class MainActivity : AppCompatActivity(), SlideListItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SlideViewModel
    private lateinit var slideAdapter: SlideListAdapter
    private val getImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::handleImageResult)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initListeners()
        initRecyclerView()
        initViewModel()

        viewModel.loadSlidesState()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveSlidesState()

        val rootView = window.decorView.rootView
        val bitmap = getBitmapFromView(rootView)

        saveBitmap(bitmap)
    }
    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private fun saveBitmap(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "screenshot.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.flush()
            outputStream?.close()
        }
    }

    private fun handleImageResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri = result.data?.data
            handleImageSlide(selectedImageUri)
        }
    }

    private fun handleImageSlide(selectedImageUri: Uri?) {
        val selectedSlide = viewModel.selectedSlide.value

        if (selectedSlide is ImageSlide) {
            val imageBytes = selectedImageUri?.let { uriToByteArray(it) }
            imageBytes?.let {
                selectedSlide.imageBytes = it
                viewModel.changeImgByteArr(selectedSlide, it)
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
        binding.incRightMenu.viewModel = viewModel
        binding.incSlideList.viewModel = viewModel

        viewModel.slides.observe(this) { slides ->
            slides?.forEach { slide ->
                if (!slideAdapter.itemList.any { it.id == slide.id }) {
                    slideAdapter.addItem(slide)
                    makeSlideView(slide)
                }
            }
        }

        viewModel.selectedSlideIndex?.observe(this) { index ->
            index?.let {
                slideAdapter.slideClickItemUpdate(it)
            } ?: run {
                slideAdapter.slideClickItemUpdate(-1)
            }
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
            slideAdapter.selectedPosition = -1
            binding.incRightMenu.etBackground.text = ""
            binding.incRightMenu.tvAlpha.text = ""
        }

        binding.incRightMenu.btnNewDoc.setOnClickListener {
            viewModel.processAction(SlideAction.ClearAllSlide)
            slideAdapter.clearItem()
            binding.boardView.removeAllViews()
        }
    }

    private fun makeSlideView(newSlide: Slide) {
        val slideView = when (newSlide) {
            is ImageSlide -> ImageBaseSlideView(this, newSlide.id, viewModel)
            is SquareSlide -> SquareBaseSlideView(this, newSlide.id, viewModel)
            is DrawingSlide -> DrawingBaseSlideView(this,  newSlide.id, viewModel)
            else -> throw IllegalArgumentException("Unknown slide type")
        }

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
        binding.incSlideList.rvSlide.itemAnimator = SlideInLeftAnimator()

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

    private fun uriToByteArray(uri: Uri): ByteArray {
        val inputStream = contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: byteArrayOf()
    }

}