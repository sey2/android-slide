package com.example.slide

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.slide.databinding.ActivityMainBinding
import com.example.slide.factory.SquareFactory
import com.example.slide.model.SlideViewModel
import com.example.slide.util.ColorUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SlideViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViewModel()
        initListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[SlideViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.selectedSlide.observe(this, Observer { slide ->
            slide?.let {
                binding.slideView.changeColor(it.backgroundColor, it.alpha * 10)
                binding.incRightMenu.etBackground.setBackgroundColor(it.backgroundColor)
                val hex = Integer.toHexString(it.backgroundColor)
                binding.incRightMenu.etBackground.text = "0x${hex.uppercase()}"
                binding.incRightMenu.tvAlpha.text = it.alpha.toString()
            }
        })

        viewModel.slides.observe(this, Observer { slides ->
            slides?.let {
                val selectedSlide = viewModel.selectedSlide.value

                selectedSlide?.let {
                    binding.slideView.changeColor(it.backgroundColor, it.alpha * 10)
                    val hex = Integer.toHexString(it.backgroundColor)
                    binding.incRightMenu.etBackground.text = "0x${hex.uppercase()}"
                    binding.incRightMenu.tvAlpha.text = it.alpha.toString()
                }

            }
        })

        viewModel.slideManager.addSlide(SquareFactory.createSquareSlide(212))
        viewModel.selectSlide(0)
    }

    private fun initListeners() {
        binding.boardView.setOnClickListener {
            binding.slideView.isSelected = false
        }

        binding.incRightMenu.etBackground.setOnClickListener {
            val color = ColorUtil.generateRandomColor()
            viewModel.changeColor(0, color)
            binding.incRightMenu.etBackground.setBackgroundColor(color)

            val hex = Integer.toHexString(color)
            binding.incRightMenu.etBackground.text = "0x${hex.uppercase()}"
        }

        binding.incRightMenu.tvPlus.setOnClickListener {
            val selectedSlide = viewModel.selectedSlide.value

            selectedSlide?.let {
                viewModel.changeAlpha(index = 0, selectedSlide.alpha + 1)
            }
        }

        binding.incRightMenu.tvMinus.setOnClickListener {
            val selectedSlide = viewModel.selectedSlide.value

            selectedSlide?.let {
                viewModel.changeAlpha(index = 0, selectedSlide.alpha - 1)
            }
        }

    }


}