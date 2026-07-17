package com.example.lifeos.ui.screens.belajar.bahasainggris.listening

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import java.util.Locale

enum class ListeningAnswerStatus { UNANSWERED, CORRECT, INCORRECT }

private const val UTTERANCE_ID = "listening_question"

class ListeningViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private val statuses = mutableStateMapOf<Int, ListeningAnswerStatus>()
    private val selectedChoices = mutableStateMapOf<Int, Int>()

    var isTtsReady by mutableStateOf(false)
        private set

    val total: Int = listeningQuestionBank.size

    val score: Int
        get() = statuses.values.count { it == ListeningAnswerStatus.CORRECT }

    init {
        tts = TextToSpeech(application, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isTtsReady = true
        }
    }

    fun speak(text: String) {
        if (!isTtsReady) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
    }

    fun statusOf(questionId: Int): ListeningAnswerStatus = statuses[questionId] ?: ListeningAnswerStatus.UNANSWERED

    fun selectedChoiceOf(questionId: Int): Int? = selectedChoices[questionId]

    fun submitAnswer(questionId: Int, choiceIndex: Int, correctIndex: Int) {
        if (statuses[questionId] != null) return
        selectedChoices[questionId] = choiceIndex
        statuses[questionId] = if (choiceIndex == correctIndex) {
            ListeningAnswerStatus.CORRECT
        } else {
            ListeningAnswerStatus.INCORRECT
        }
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        super.onCleared()
    }
}
