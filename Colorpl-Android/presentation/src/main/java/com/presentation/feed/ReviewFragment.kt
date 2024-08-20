package com.presentation.feed

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReviewBinding
import com.domain.model.Review
import com.google.mediapipe.tasks.text.textclassifier.TextClassifierResult
import com.presentation.base.BaseDialogFragment
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.SpoilerWeightClassifier
import com.presentation.util.getPhotoGallery
import com.presentation.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class ReviewFragment : BaseDialogFragment<FragmentReviewBinding>(R.layout.fragment_review) {

    private lateinit var emotions: List<ImageView>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var photoUri: Uri
    private val viewModel: ReviewViewModel by viewModels()
    private val args: ReviewFragmentArgs by navArgs()
    private lateinit var classifier: SpoilerWeightClassifier


    private val listener = object :
        SpoilerWeightClassifier.TextResultsListener {
        override fun onResult(
            results: TextClassifierResult,
            inferenceTime: Long
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                val res = results.classificationResult()
                    .classifications().first()
                    .categories().first()
                if ((res.index() == 0 && res.score() < 0.6) || res.index() == 1) {
                    Timber.tag("tensorflow").d("${viewModel.spoilerWeight.value}")
                    viewModel.setSpoilerWeight(
                        1
                    )
                } else {
                    viewModel.setSpoilerWeight(
                        0
                    )
                }

                Timber.tag("tensorflow").d(
                    "${
                        results.classificationResult()
                            .classifications().first()
                            .categories()
                    }"
                )
            }
        }

        override fun onError(error: String) {
            Timber.d("에러 확인 $error")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classifier = SpoilerWeightClassifier(
            context = requireContext(),
            listener = listener
        )
    }


    override fun initView(savedInstanceState: Bundle?) {
        observeViewModel()
        initGalleryPhoto()
        initUi()
        initEmotion()
        checkEditText()
    }

    private fun initUi() {
        binding.tvConfirm.setOnClickListener {
            if (viewModel.confirmCheck.value) {
                createReview()
            }
        }
        binding.ivEnroll.setOnClickListener {
            getPhotoGallery(pickImageLauncher)
        }

        binding.etContent.addTextChangedListener { s ->
            if (s.toString().length > 10) {
                classifier.classify(s.toString())
            }
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

    private fun createReview() {
        val image = if (::photoUri.isInitialized) {
            ImageProcessingUtil(binding.root.context).uriToCompressedFile(photoUri)
        } else {
            null
        }
        viewModel.createReview(
            review = Review(
                args.ticketId,
                binding.etContent.text.toString(),
                viewModel.spoilerWeight.value,
                viewModel.selectedEmotion.value + 1
            ),
            image
        )

        showLoading()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.confirmCheck.collectLatest { state ->
                    binding.tvConfirm.isSelected = state
                }
            }

            launch {
                viewModel.reviewResponse.collectLatest { reviewId ->
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "refresh",
                        true
                    )
                    dismissLoading()
                    if (reviewId > 0) navigatePopBackStack() else {
                        Toast.makeText(requireContext(), "리뷰 등록에 실패했습니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun initGalleryPhoto() {
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.data?.let { uri ->
                        photoUri = uri
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

    override fun onDestroy() {
        super.onDestroy()
    }
}