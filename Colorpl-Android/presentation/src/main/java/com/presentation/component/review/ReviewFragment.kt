package com.presentation.component.review

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReviewBinding
import com.presentation.base.BaseDialogFragment
import com.presentation.util.getPhotoGallery
import com.presentation.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ReviewFragment : BaseDialogFragment<FragmentReviewBinding>(R.layout.fragment_review) {

    private lateinit var emotions: List<ImageView>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private val viewModel: ReviewViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        initGalleryPhoto()
        initUi()
        observeViewModel()
        initEmotion()
        checkEditText()
    }

    private fun initUi() {
        binding.tvConfirm.setOnClickListener {
            // action
            dismiss()
        }
        binding.ivEnroll.setOnClickListener {
            getPhotoGallery(pickImageLauncher)
        }
    }

    private fun initEmotion() {
        emotions = listOf(
            binding.ivExcited,
            binding.ivLove,
            binding.ivTired,
            binding.ivCrying,
            binding.ivAngry
        )

        emotions.forEachIndexed { index, emotion ->
            emotion.setOnClickListener {
                toggleEmotion(index)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.confirmCheck.collectLatest { state ->
                    binding.tvConfirm.isSelected = state
                }
            }
        }
    }

    private fun initGalleryPhoto() {
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.data?.let { uri ->
                        Glide.with(binding.root.context).load(uri).centerCrop()
                            .into(binding.ivReview)
                        binding.ivEnroll.visibility = View.GONE
                    }
                }
            }
    }

    private fun checkEditText() {
        binding.etContent.addTextChangedListener {
            viewModel.checkEditText(it?.length)
        }
    }

    /** 이전에 선택된 이미지가 있다면 선택 해제
     *  새로 클릭된 이미지를 선택 상태로 변경
     *  같은 이미지를 다시 클릭하면 선택 해제
     */
    private fun toggleEmotion(clickedIndex: Int) {
        if (viewModel.selectedEmotion.value != -1) {
            emotions[viewModel.selectedEmotion.value].isSelected = false
        }

        if (viewModel.selectedEmotion.value != clickedIndex) {
            emotions[clickedIndex].isSelected = true
            viewModel.setEmotion(clickedIndex)
        } else {
            viewModel.setEmotion(-1)
        }

        emotions.forEachIndexed { index, emotion ->
            emotion.isSelected = index == viewModel.selectedEmotion.value
        }
    }

}