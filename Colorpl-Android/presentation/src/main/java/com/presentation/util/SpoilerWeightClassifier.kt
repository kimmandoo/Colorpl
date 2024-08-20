package com.presentation.util

import android.content.Context
import android.os.SystemClock
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.text.textclassifier.TextClassifier
import com.google.mediapipe.tasks.text.textclassifier.TextClassifierResult
import timber.log.Timber
import java.util.concurrent.ScheduledThreadPoolExecutor

private const val TAG = "SpoilerWeightClassifier"

class SpoilerWeightClassifier(
    val context: Context,
    val listener: TextResultsListener
) {
    private lateinit var textClassifier: TextClassifier
    private lateinit var executor: ScheduledThreadPoolExecutor

    init {
        initClassifier()
    }

    private fun initClassifier() {
        val baseOptionsBuilder = BaseOptions.builder()
            .setModelAssetPath("bert_classifier.tflite")

        try {
            val baseOptions = baseOptionsBuilder.build()
            val optionsBuilder = TextClassifier.TextClassifierOptions.builder()
                .setBaseOptions(baseOptions)
            val options = optionsBuilder.build()
            textClassifier = TextClassifier.createFromOptions(context, options)
        } catch (e: IllegalStateException) {
            listener.onError(
                "Text classifier failed to initialize. See error logs for $e"
            )

        }
    }

    fun classify(text: String) {
        executor = ScheduledThreadPoolExecutor(1)

        executor.execute {
            var inferenceTime = SystemClock.uptimeMillis()

            val results = textClassifier.classify(text)

            inferenceTime = SystemClock.uptimeMillis() - inferenceTime
            listener.onResult(results, inferenceTime)
        }
    }

    interface TextResultsListener {
        fun onError(error: String)
        fun onResult(results: TextClassifierResult, inferenceTime: Long)
    }
}