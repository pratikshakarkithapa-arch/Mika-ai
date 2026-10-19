package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaVibrantPink
import kotlin.math.sin

@Composable
fun AudioWaveform(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    barCount: Int = 36
) {
    val infiniteTransition = rememberInfiniteTransition(label = "WaveformAnim")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f
        val step = width / (barCount + 1)

        val gradient = Brush.horizontalGradient(
            colors = listOf(
                MikaVibrantPink.copy(alpha = 0.5f),
                MikaNeonPurpleLight.copy(alpha = glowAlpha),
                MikaNeonPurple.copy(alpha = glowAlpha),
                MikaVibrantPink.copy(alpha = 0.5f)
            )
        )

        for (i in 0 until barCount) {
            val x = step * (i + 1)
            // Gaussian bell envelope so center bars are taller
            val normalizedX = (i.toFloat() / barCount - 0.5f) * 2f
            val envelope = kotlin.math.exp(-3f * normalizedX * normalizedX)

            val wave = if (isActive) {
                sin(phase + i * 0.45f) * 0.5f + 0.5f
            } else {
                0.15f
            }

            val barHeight = ((height * 0.85f * envelope * wave).coerceAtLeast(4.dp.toPx()))

            drawLine(
                brush = gradient,
                start = Offset(x, centerY - barHeight / 2),
                end = Offset(x, centerY + barHeight / 2),
                strokeWidth = 3.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}
