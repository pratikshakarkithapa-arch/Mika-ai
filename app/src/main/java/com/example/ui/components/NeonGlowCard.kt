package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.MikaSentiment
import com.example.data.model.pulseDurationMillis
import com.example.data.model.pulseIntensityMultiplier
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaSurfaceBorder
import com.example.ui.theme.MikaSurfaceDark
import com.example.ui.theme.getSentimentGlowColor

@Composable
fun NeonGlowCard(
    modifier: Modifier = Modifier,
    sentiment: MikaSentiment = MikaSentiment.HAPPY,
    glowColor: Color = MikaNeonPurple,
    containerColor: Color = MikaSurfaceDark,
    cornerRadius: Dp = 20.dp,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val duration = sentiment.pulseDurationMillis
    val intensity = sentiment.pulseIntensityMultiplier

    val infiniteTransition = rememberInfiniteTransition(label = "NeonPulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.25f * intensity,
        targetValue = 0.85f * intensity.coerceAtMost(1.15f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "neonAlpha"
    )

    val activeColor = getSentimentGlowColor(sentiment)

    Surface(
        modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
        shape = RoundedCornerShape(cornerRadius),
        color = containerColor,
        border = BorderStroke(
            width = borderWidth,
            color = activeColor.copy(alpha = alphaAnim.coerceIn(0.2f, 1f))
        ),
        shadowElevation = 8.dp
    ) {
        Box {
            content()
        }
    }
}
