package com.example.lifeos.ui.screens.belajar.matematika.speedmath

import kotlin.random.Random

private const val MIN_OPERAND = 1
private const val MAX_ADD_SUB_OPERAND = 50
private const val MAX_MUL_DIV_OPERAND = 12

private const val START_MILLIS = 10_000L
private const val MIN_MILLIS = 3_000L
private const val DECREASE_PER_QUESTION_MILLIS = 400L

enum class MathOperation { ADD, SUBTRACT, MULTIPLY, DIVIDE }

data class MathQuestion(val text: String, val correctAnswer: Int, val choices: List<Int>)

fun generateQuestion(random: Random = Random.Default): MathQuestion {
    val (text, answer) = when (MathOperation.entries.random(random)) {
        MathOperation.ADD -> {
            val a = random.nextInt(MIN_OPERAND, MAX_ADD_SUB_OPERAND + 1)
            val b = random.nextInt(MIN_OPERAND, MAX_ADD_SUB_OPERAND + 1)
            "$a + $b" to a + b
        }
        MathOperation.SUBTRACT -> {
            val a = random.nextInt(MIN_OPERAND, MAX_ADD_SUB_OPERAND + 1)
            val b = random.nextInt(MIN_OPERAND, a + 1)
            "$a - $b" to a - b
        }
        MathOperation.MULTIPLY -> {
            val a = random.nextInt(MIN_OPERAND, MAX_MUL_DIV_OPERAND + 1)
            val b = random.nextInt(MIN_OPERAND, MAX_MUL_DIV_OPERAND + 1)
            "$a × $b" to a * b
        }
        MathOperation.DIVIDE -> {
            val divisor = random.nextInt(MIN_OPERAND, MAX_MUL_DIV_OPERAND + 1)
            val quotient = random.nextInt(MIN_OPERAND, MAX_MUL_DIV_OPERAND + 1)
            "${divisor * quotient} ÷ $divisor" to quotient
        }
    }
    return MathQuestion(text = text, correctAnswer = answer, choices = generateChoices(answer, random))
}

private fun generateChoices(correct: Int, random: Random): List<Int> {
    val choices = linkedSetOf(correct)
    var attempts = 0
    while (choices.size < 4 && attempts < 50) {
        attempts++
        val delta = random.nextInt(1, 11) * if (random.nextBoolean()) 1 else -1
        val candidate = correct + delta
        if (candidate >= 0) choices.add(candidate)
    }
    var filler = correct + 1
    while (choices.size < 4) {
        choices.add(filler)
        filler++
    }
    return choices.shuffled(random)
}

/** Time budget for the question after [correctAnswerCount] correct answers so far; shrinks each round. */
fun timeForQuestion(correctAnswerCount: Int): Long =
    (START_MILLIS - correctAnswerCount * DECREASE_PER_QUESTION_MILLIS).coerceAtLeast(MIN_MILLIS)
