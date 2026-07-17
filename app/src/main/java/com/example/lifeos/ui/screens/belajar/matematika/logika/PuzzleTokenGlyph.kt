package com.example.lifeos.ui.screens.belajar.matematika.logika

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/** Renders a [PuzzleToken] as a vector shape drawn on a [Canvas] — no image assets needed. */
@Composable
fun PuzzleTokenGlyph(token: PuzzleToken, modifier: Modifier = Modifier, unitSize: Dp = 48.dp) {
    val color = token.color.resolve()
    if (token.count <= 1) {
        Canvas(modifier = modifier.size(unitSize * token.scale)) {
            drawPuzzleShape(token.shape, color)
        }
    } else {
        Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(token.count) {
                Canvas(modifier = Modifier.size(unitSize * token.scale / 1.8f)) {
                    drawPuzzleShape(token.shape, color)
                }
            }
        }
    }
}

private fun DrawScope.drawPuzzleShape(shape: TokenShape, color: Color) {
    val w = size.width
    val h = size.height
    when (shape) {
        TokenShape.CIRCLE -> drawCircle(color = color, radius = w / 2.2f, center = Offset(w / 2f, h / 2f))
        TokenShape.SQUARE -> {
            val inset = w * 0.1f
            drawRect(color = color, topLeft = Offset(inset, inset), size = Size(w - inset * 2, h - inset * 2))
        }
        TokenShape.TRIANGLE -> {
            val path = Path().apply {
                moveTo(w / 2f, h * 0.06f)
                lineTo(w * 0.94f, h * 0.94f)
                lineTo(w * 0.06f, h * 0.94f)
                close()
            }
            drawPath(path, color = color)
        }
        TokenShape.DIAMOND -> {
            val path = Path().apply {
                moveTo(w / 2f, h * 0.04f)
                lineTo(w * 0.96f, h / 2f)
                lineTo(w / 2f, h * 0.96f)
                lineTo(w * 0.04f, h / 2f)
                close()
            }
            drawPath(path, color = color)
        }
        TokenShape.HEXAGON -> {
            val path = Path()
            val cx = w / 2f
            val cy = h / 2f
            val r = w * 0.46f
            for (i in 0 until 6) {
                val angle = Math.toRadians((60 * i - 30).toDouble())
                val x = cx + (r * cos(angle)).toFloat()
                val y = cy + (r * sin(angle)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = color)
        }
        TokenShape.STAR -> {
            val path = Path()
            val cx = w / 2f
            val cy = h / 2f
            val outerR = w * 0.48f
            val innerR = outerR * 0.42f
            for (i in 0 until 10) {
                val r = if (i % 2 == 0) outerR else innerR
                val angle = Math.toRadians((36 * i - 90).toDouble())
                val x = cx + (r * cos(angle)).toFloat()
                val y = cy + (r * sin(angle)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = color)
        }
    }
}
