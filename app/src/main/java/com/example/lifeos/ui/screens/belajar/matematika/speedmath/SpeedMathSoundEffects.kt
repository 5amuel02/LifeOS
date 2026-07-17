package com.example.lifeos.ui.screens.belajar.matematika.speedmath

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

private const val SAMPLE_RATE = 44_100

private data class Note(val frequencyHz: Double, val durationMs: Int, val amplitude: Double = 0.5)

/**
 * Synthesizes short chiptune-style sound effects at runtime (sine tones with a fade
 * envelope) so the game has audio feedback without needing any bundled audio assets.
 */
class SpeedMathSoundEffects {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun playStart() = playMelody(listOf(Note(523.25, 80), Note(659.25, 80), Note(783.99, 130)))

    fun playCorrect() = playMelody(listOf(Note(880.0, 60), Note(1174.66, 90)))

    fun playWrong() = playMelody(listOf(Note(220.0, 90), Note(164.81, 170)))

    /** [urgency] 0..1, higher pitch and volume as the question's time runs out. */
    fun playTick(urgency: Float) {
        val clamped = urgency.coerceIn(0f, 1f)
        playMelody(listOf(Note(700.0 + clamped * 500.0, 40, amplitude = 0.35 + clamped * 0.25)))
    }

    fun playGameOver() = playMelody(listOf(Note(392.0, 110), Note(329.63, 110), Note(261.63, 230)))

    fun playHighScore() = playMelody(
        listOf(Note(523.25, 90), Note(659.25, 90), Note(783.99, 90), Note(1046.50, 240))
    )

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
