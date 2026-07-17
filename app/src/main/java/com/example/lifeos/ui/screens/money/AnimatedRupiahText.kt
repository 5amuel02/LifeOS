package com.example.lifeos.ui.screens.money

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.lifeos.data.money.formatRupiah

@Composable
fun AnimatedRupiahText(
    amount: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
) {
    val animatedAmount = remember { Animatable(0f) }
    LaunchedEffect(amount) {
        animatedAmount.animateTo(
            targetValue = amount.toFloat(),
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
        )
    }
    Text(text = formatRupiah(animatedAmount.value.toLong()), modifier = modifier, style = style, color = color)
}
