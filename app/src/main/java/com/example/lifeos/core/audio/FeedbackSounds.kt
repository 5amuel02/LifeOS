package com.example.lifeos.core.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/** Remembers a [FeedbackSounds] instance scoped to this composable, releasing it on dispose. */
@Composable
fun rememberFeedbackSounds(): FeedbackSounds {
    val sounds = remember { FeedbackSounds() }
    DisposableEffect(sounds) {
        onDispose { sounds.release() }
    }
    return sounds
}

private const val SAMPLE_RATE = 44_100

private data class Note(val frequencyHz: Double, val durationMs: Int, val amplitude: Double = 0.5)

/**
 * Synthesizes short UI feedback sounds at runtime (sine tones with a fade envelope) so any
 * screen can get audio feedback without bundling audio assets. Shared across quizzes, habit
 * check-offs, schedule completions, and similar "did I get this right / done" moments.
 */
class FeedbackSounds {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun playCorrect() = playMelody(listOf(Note(880.0, 60), Note(1174.66, 90)))

    fun playWrong() = playMelody(listOf(Note(220.0, 90), Note(164.81, 170)))

    fun playComplete() = playMelody(
        listOf(Note(523.25, 90), Note(659.25, 90), Note(783.99, 90), Note(1046.50, 240))
    )

    fun playCheck() = playMelody(listOf(Note(659.25, 40), Note(987.77, 70)))

    fun release() {
        scope.cancel()
    }

    private fun playMelody(notes: List<Note>) {
        val samples = generateMelody(notes)
        if (samples.isEmpty()) return
        val track = buildAudioTrack(samples)
        scope.launch {
            track.play()
            delay((samples.size * 1000L / SAMPLE_RATE) + 60L)
            runCatching {
                track.stop()
                track.release()
            }
        }
    }

    private fun buildAudioTrack(samples: ShortArray): AudioTrack {
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(SAMPLE_RATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(samples.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
        track.write(samples, 0, samples.size)
        return track
    }
}

private fun generateMelody(notes: List<Note>): ShortArray {
    val buffers = notes.map { generateTone(it.frequencyHz, it.durationMs, it.amplitude) }
    val result = ShortArray(buffers.sumOf { it.size })
    var offset = 0
    buffers.forEach { buffer ->
        buffer.copyInto(result, offset)
        offset += buffer.size
    }
    return result
}

private fun generateTone(frequencyHz: Double, durationMs: Int, amplitude: Double): ShortArray {
    val sampleCount = durationMs * SAMPLE_RATE / 1000
    val samples = ShortArray(sampleCount)
    val fadeSamples = (sampleCount * 0.12).toInt().coerceAtLeast(1)
    for (i in 0 until sampleCount) {
        val t = i / SAMPLE_RATE.toDouble()
        val envelope = when {
            i < fadeSamples -> i / fadeSamples.toDouble()
            i > sampleCount - fadeSamples -> (sampleCount - i) / fadeSamples.toDouble()
            else -> 1.0
        }
        val value = sin(2.0 * PI * frequencyHz * t) * amplitude * envelope
        samples[i] = (value * Short.MAX_VALUE).toInt().toShort()
    }
    return samples
}
