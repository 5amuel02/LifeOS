package com.example.lifeos.ui.screens.belajar.matematika.speedmath

import android.app.Application
import android.media.MediaPlayer
import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.speedmath.SpeedMathRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class SpeedMathState { IDLE, PLAYING, GAME_OVER }

private const val TICK_MILLIS = 50L
private const val CORRECT_FEEDBACK_MILLIS = 200L
private const val WRONG_FEEDBACK_MILLIS = 700L
private const val URGENT_TICK_FRACTION_THRESHOLD = 0.4f

/** Background music resource name; drop `speed_math_bgm.mp3` (or .ogg) into res/raw to enable it. */
private const val BACKGROUND_MUSIC_RAW_NAME = "speed_math_bgm"

class SpeedMathViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SpeedMathRepository(LifeOSDatabase.getInstance(application).speedMathResultDao())
    private val random = Random(System.nanoTime())
    private val soundEffects = SpeedMathSoundEffects()
    private var countdownJob: Job? = null
    private var feedbackJob: Job? = null
    private var questionStartedAtRealtime = 0L
    private var totalMillisForCurrentQuestion = 0L
    private var lastUrgentTickAtRealtime = 0L
    private var backgroundMusicPlayer: MediaPlayer? = null
    private val answerTimesMillis = mutableListOf<Long>()

    var gameState by mutableStateOf(SpeedMathState.IDLE)
        private set
    var question by mutableStateOf(generateQuestion(random))
        private set
    var score by mutableIntStateOf(0)
        private set
    var remainingMillis by mutableLongStateOf(0L)
        private set
    var totalMillis by mutableLongStateOf(0L)
        private set
    var selectedAnswer by mutableStateOf<Int?>(null)
        private set
    var lastScore by mutableIntStateOf(0)
        private set
    var lastAverageAnswerMillis by mutableLongStateOf(0L)
        private set
    var isNewHighScore by mutableStateOf(false)
        private set

    val bestScore: StateFlow<Int> = repository.getBestResult()
        .map { it?.score ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun startGame() {
        countdownJob?.cancel()
        feedbackJob?.cancel()
        score = 0
        answerTimesMillis.clear()
        gameState = SpeedMathState.PLAYING
        soundEffects.playStart()
        startBackgroundMusicIfAvailable()
        nextQuestion()
    }

    fun submitAnswer(answer: Int) {
        if (gameState != SpeedMathState.PLAYING || selectedAnswer != null) return
        countdownJob?.cancel()
        selectedAnswer = answer
        val elapsed = SystemClock.elapsedRealtime() - questionStartedAtRealtime

        if (answer == question.correctAnswer) {
            answerTimesMillis.add(elapsed)
            score += 1
            soundEffects.playCorrect()
            feedbackJob = viewModelScope.launch {
                delay(CORRECT_FEEDBACK_MILLIS)
                nextQuestion()
            }
        } else {
            soundEffects.playWrong()
            feedbackJob = viewModelScope.launch {
                delay(WRONG_FEEDBACK_MILLIS)
                endGame()
            }
        }
    }

    private fun nextQuestion() {
        selectedAnswer = null
        question = generateQuestion(random)
        totalMillisForCurrentQuestion = timeForQuestion(score)
        totalMillis = totalMillisForCurrentQuestion
        remainingMillis = totalMillisForCurrentQuestion
        questionStartedAtRealtime = SystemClock.elapsedRealtime()
        lastUrgentTickAtRealtime = 0L
        countdownJob = viewModelScope.launch {
            while (remainingMillis > 0) {
                delay(TICK_MILLIS)
                val elapsed = SystemClock.elapsedRealtime() - questionStartedAtRealtime
                remainingMillis = (totalMillisForCurrentQuestion - elapsed).coerceAtLeast(0)
                maybePlayUrgentTick()
            }
            if (gameState == SpeedMathState.PLAYING && selectedAnswer == null) {
                endGame()
            }
        }
    }

    /** Plays a heartbeat-style tick that speeds up and rises in pitch as time runs out. */
    private fun maybePlayUrgentTick() {
        if (totalMillisForCurrentQuestion == 0L) return
        val fraction = remainingMillis.toFloat() / totalMillisForCurrentQuestion.toFloat()
        if (fraction >= URGENT_TICK_FRACTION_THRESHOLD) return
        val now = SystemClock.elapsedRealtime()
        val interval = 120L + (fraction / URGENT_TICK_FRACTION_THRESHOLD * 230L).toLong()
        if (now - lastUrgentTickAtRealtime >= interval) {
            lastUrgentTickAtRealtime = now
            soundEffects.playTick(urgency = 1f - fraction)
        }
    }

    private fun endGame() {
        countdownJob?.cancel()
        gameState = SpeedMathState.GAME_OVER
        lastScore = score
        lastAverageAnswerMillis = if (answerTimesMillis.isEmpty()) 0L else answerTimesMillis.average().toLong()
        soundEffects.playGameOver()
        stopBackgroundMusic()
        viewModelScope.launch {
            val previousBest = repository.getBestScoreOnce()
            isNewHighScore = lastScore > previousBest
            repository.recordResult(lastScore, lastAverageAnswerMillis)
            if (isNewHighScore) {
                delay(250)
                soundEffects.playHighScore()
            }
        }
    }

    private fun startBackgroundMusicIfAvailable() {
        if (backgroundMusicPlayer != null) return
        val context = getApplication<Application>()
        val resId = context.resources.getIdentifier(BACKGROUND_MUSIC_RAW_NAME, "raw", context.packageName)
        if (resId == 0) return
        backgroundMusicPlayer = runCatching {
            MediaPlayer.create(context, resId)?.apply {
                isLooping = true
                setVolume(0.35f, 0.35f)
                start()
            }
        }.getOrNull()
    }

    private fun stopBackgroundMusic() {
        backgroundMusicPlayer?.let { player ->
            runCatching {
                player.stop()
                player.release()
            }
        }
        backgroundMusicPlayer = null
    }

    override fun onCleared() {
        countdownJob?.cancel()
        feedbackJob?.cancel()
        soundEffects.release()
        stopBackgroundMusic()
        super.onCleared()
    }
}
