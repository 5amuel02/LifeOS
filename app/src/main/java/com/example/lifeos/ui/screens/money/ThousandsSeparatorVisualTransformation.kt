package com.example.lifeos.ui.screens.money

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Displays a plain-digit amount string (e.g. "1000000") with dot thousands separators
 * (e.g. "1.000.000") while the underlying field value stays pure digits for calculation.
 */
class ThousandsSeparatorVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        if (original.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val formatted = StringBuilder()
        val originalToTransformed = IntArray(original.length + 1)
        val totalDigits = original.length

        for (i in original.indices) {
            formatted.append(original[i])
            originalToTransformed[i + 1] = formatted.length
            val digitsFromEnd = totalDigits - (i + 1)
            if (digitsFromEnd > 0 && digitsFromEnd % 3 == 0) {
                formatted.append('.')
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val clamped = offset.coerceIn(0, original.length)
                return originalToTransformed[clamped]
            }

            override fun transformedToOriginal(offset: Int): Int {
                val clamped = offset.coerceIn(0, formatted.length)
                var count = 0
                for (i in 0 until clamped) {
                    if (formatted[i] != '.') count++
                }
                return count.coerceIn(0, original.length)
            }
        }

        return TransformedText(AnnotatedString(formatted.toString()), offsetMapping)
    }
}
