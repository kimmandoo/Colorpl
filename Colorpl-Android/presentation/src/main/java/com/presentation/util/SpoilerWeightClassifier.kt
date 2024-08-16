package com.presentation.util

import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.text.textclassifier.TextClassifier
import com.google.mediapipe.tasks.text.textclassifier.TextClassifierResult
import org.tensorflow.lite.Interpreter
import timber.log.Timber
import java.util.concurrent.ScheduledThreadPoolExecutor

class SpoilerWeightClassifier(
    val context: Context,
    val listener: TextResultsListener
) {
    private lateinit var textClassifier: TextClassifier
    private lateinit var executor: ScheduledThreadPoolExecutor

    init {
        initClassifier()
    }

    fun initClassifier() {
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
                "Text classifier failed to initialize. See error logs for " +
                        "details"
            )
            Timber.e(
                TAG, "Text classifier failed to load the task with error: " + e
                    .message
            )
        }
    }

    // Run text classification using MediaPipe Text Classifier API
    fun classify(text: String) {
        executor = ScheduledThreadPoolExecutor(1)

        executor.execute {
            // inferenceTime is the amount of time, in milliseconds, that it takes to
            // classify the input text.
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

    companion object {
        const val TAG = "TextClassifierHelper"
    }
}