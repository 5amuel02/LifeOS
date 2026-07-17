package com.example.lifeos.ui.screens.beranda

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private fun lerp(start: Float, end: Float, fraction: Float): Float = start + (end - start) * fraction

private fun lerpOffset(start: Offset, end: Offset, fraction: Float): Offset =
    Offset(lerp(start.x, end.x, fraction), lerp(start.y, end.y, fraction))

@Composable
fun ExercisingStickman(modifier: Modifier = Modifier, size: Dp = 44.dp) {
    val transition = rememberInfiniteTransition(label = "exercisingStickman")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jumpingJackProgress"
    )
    val color = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.size(size)) {
        drawExercisingStickman(progress = progress, color = color)
    }
}

@Composable
fun StudyingStickman(modifier: Modifier = Modifier, size: Dp = 44.dp) {
    val transition = rememberInfiniteTransition(label = "studyingStickman")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "writingProgress"
    )
    val color = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.size(size)) {
        drawStudyingStickman(progress = progress, color = color)
    }
}

// Jumping jack dengan sendi siku/lutut supaya gerakannya terlihat natural,
// plus lompatan kecil dan bayangan yang mengecil saat badan terangkat.
private fun DrawScope.drawExercisingStickman(progress: Float, color: Color) {
    val sx = size.width / 60f
    val sy = size.height / 80f
    val stroke = size.width * 0.055f
    val jump = -4f * progress
    fun p(x: Float, y: Float) = Offset(x * sx, (y + jump) * sy)

    // Bayangan di lantai (tidak ikut melompat)
    val shadowWidth = lerp(20f, 14f, progress) * sx
    drawOval(
        color = color.copy(alpha = 0.15f),
        topLeft = Offset(30f * sx - shadowWidth / 2, 76f * sy),
        size = Size(shadowWidth, 3.5f * sy)
    )

    val head = p(30f, 16f)
    val neck = p(30f, 26f)
    val hip = p(30f, 50f)

    // Lengan: bahu -> siku -> tangan (turun di samping badan / naik ke atas kepala)
    val leftElbow = lerpOffset(p(25f, 36f), p(21f, 17f), progress)
    val leftHand = lerpOffset(p(24f, 45f), p(25f, 6f), progress)
    val rightElbow = lerpOffset(p(35f, 36f), p(39f, 17f), progress)
    val rightHand = lerpOffset(p(36f, 45f), p(35f, 6f), progress)

    // Kaki: pinggul -> lutut -> kaki (rapat / terbuka)
    val leftKnee = lerpOffset(p(28f, 62f), p(23f, 61f), progress)
    val leftFoot = lerpOffset(p(28f, 74f), p(19f, 73f), progress)
    val rightKnee = lerpOffset(p(32f, 62f), p(37f, 61f), progress)
    val rightFoot = lerpOffset(p(32f, 74f), p(41f, 73f), progress)

    drawCircle(color = color, radius = 5.5f * sx, center = head)
    drawLine(color, neck, hip, stroke, cap = StrokeCap.Round)
    drawLimb(color, neck, leftElbow, leftHand, stroke)
    drawLimb(color, neck, rightElbow, rightHand, stroke)
    drawLimb(color, hip, leftKnee, leftFoot, stroke)
    drawLimb(color, hip, rightKnee, rightFoot, stroke)
}

// Duduk membungkuk di meja: kepala mengangguk pelan dan tangan kanan
// bergerak menulis di atas buku, tangan kiri menahan buku.
private fun DrawScope.drawStudyingStickman(progress: Float, color: Color) {
    val sx = size.width / 60f
    val sy = size.height / 80f
    val stroke = size.width * 0.055f
    fun p(x: Float, y: Float) = Offset(x * sx, y * sy)

    val floorY = 66f

    // Meja
    drawLine(color, p(30f, 46f), p(54f, 46f), stroke * 0.8f, cap = StrokeCap.Round)
    drawLine(color, p(33f, 46f), p(33f, floorY), stroke * 0.7f, cap = StrokeCap.Round)
    drawLine(color, p(51f, 46f), p(51f, floorY), stroke * 0.7f, cap = StrokeCap.Round)

    // Buku terbuka di atas meja
    val bookPath = Path().apply {
        moveTo(36f * sx, 44.5f * sy)
        lineTo(42f * sx, 42.5f * sy)
        lineTo(48f * sx, 44.5f * sy)
    }
    drawPath(bookPath, color, style = Stroke(width = stroke * 0.7f, cap = StrokeCap.Round))
    drawLine(color, p(42f, 42.5f), p(42f, 44.5f), stroke * 0.5f, cap = StrokeCap.Round)

    // Kursi
    drawLine(color, p(14f, 50f), p(24f, 50f), stroke * 0.8f, cap = StrokeCap.Round)
    drawLine(color, p(16f, 50f), p(16f, floorY), stroke * 0.7f, cap = StrokeCap.Round)
    drawLine(color, p(22f, 50f), p(22f, floorY), stroke * 0.7f, cap = StrokeCap.Round)

    // Badan duduk agak condong ke depan, kepala mengangguk pelan
    val nod = 1.2f * progress
    val head = p(25f + nod * 0.6f, 22f + nod)
    val neck = p(23f, 30f)
    val hip = p(19f, 49f)

    // Kaki duduk: paha ke depan, betis turun ke lantai
    val knee = p(28f, 52f)
    val foot = p(28f, 64f)

    // Tangan kiri menahan tepi buku, tangan kanan menulis (bergerak kecil)
    val restElbow = p(28f, 38f)
    val restHand = p(35f, 44f)
    val writeElbow = lerpOffset(p(31f, 39f), p(32f, 38f), progress)
    val writeHand = lerpOffset(p(38f, 43f), p(45f, 42.5f), progress)

    drawCircle(color = color, radius = 5f * sx, center = head)
    drawLine(color, neck, hip, stroke, cap = StrokeCap.Round)
    drawLimb(color, hip, knee, foot, stroke)
    drawLimb(color, neck, restElbow, restHand, stroke * 0.9f)
    drawLimb(color, neck, writeElbow, writeHand, stroke * 0.9f)

    // Pensil kecil di tangan yang menulis
    drawLine(
        color,
        writeHand,
        Offset(writeHand.x + 2.5f * sx, writeHand.y + 2f * sy),
        stroke * 0.6f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawLimb(color: Color, from: Offset, joint: Offset, to: Offset, stroke: Float) {
    drawLine(color, from, joint, stroke, cap = StrokeCap.Round)
    drawLine(color, joint, to, stroke, cap = StrokeCap.Round)
}
