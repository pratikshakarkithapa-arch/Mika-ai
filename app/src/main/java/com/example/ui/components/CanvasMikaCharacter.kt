package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.MikaAvatarState
import com.example.data.model.MikaSentiment
import com.example.data.model.pulseIntensityMultiplier
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaVibrantPink
import com.example.ui.theme.getSentimentGlowColor
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun CanvasMikaCharacter(
    modifier: Modifier = Modifier,
    avatarState: MikaAvatarState = MikaAvatarState.IDLE,
    sentiment: MikaSentiment = MikaSentiment.HAPPY,
    stageSize: Dp = 0.dp,
    showGlowRing: Boolean = true,
    roundedCorners: Dp = 24.dp,
    isCircle: Boolean = false,
    onCharacterClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "NaturalMikaLoop")
    val masterPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "masterPhase"
    )

    val breathPhase = masterPhase * 2.0
    val breathWave = (sin(breathPhase) + 0.15 * sin(breathPhase * 2.0)).toFloat()
    val verticalBreathingLift = -1.4f * breathWave

    var isRandomBlinking by remember { mutableStateOf(false) }

    LaunchedEffect(avatarState) {
        if (avatarState == MikaAvatarState.LISTENING) {
            isRandomBlinking = true
            return@LaunchedEffect
        }

        while (true) {
            val intervalMs = Random.nextLong(2400L, 5600L)
            delay(intervalMs)

            if (avatarState != MikaAvatarState.SPEAKING) {
                isRandomBlinking = true
                delay(170)
                isRandomBlinking = false

                if (Random.nextFloat() < 0.20f) {
                    delay(120)
                    isRandomBlinking = true
                    delay(150)
                    isRandomBlinking = false
                }
            }
        }
    }

    val glowColor = getSentimentGlowColor(sentiment)

    Box(
        modifier = modifier
            .then(if (stageSize > 0.dp) Modifier.size(stageSize) else Modifier.fillMaxSize())
            .then(
                if (onCharacterClick != null) Modifier.clickable { onCharacterClick() }
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        val containerShape = if (isCircle) CircleShape else RoundedCornerShape(roundedCorners)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(containerShape)
                .background(Color(0xFF0F0B1E))
        ) {
            // Stationary character image with delicate breathing lift
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = verticalBreathingLift
                    }
            ) {
                Crossfade(
                    targetState = isRandomBlinking || avatarState == MikaAvatarState.LISTENING,
                    animationSpec = tween(durationMillis = 140),
                    label = "BlinkCrossfade"
                ) { isBlinking ->
                    val imageRes = if (isBlinking) {
                        R.drawable.mika_bedroom_blink
                    } else {
                        R.drawable.mika_bedroom_portrait
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Mika AI",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Canvas for hair shimmer, soft blush and sentiment motes
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Gentle blush
                val blushAlpha = when (sentiment) {
                    MikaSentiment.LOVING -> 0.40f
                    MikaSentiment.EXCITED -> 0.30f
                    MikaSentiment.PLAYFUL -> 0.25f
                    MikaSentiment.HAPPY -> 0.20f
                    else -> 0.12f
                }

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MikaVibrantPink.copy(alpha = blushAlpha),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.38f, h * 0.46f),
                        radius = w * 0.14f
                    ),
                    radius = w * 0.14f,
                    center = Offset(w * 0.38f, h * 0.46f)
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MikaVibrantPink.copy(alpha = blushAlpha),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.62f, h * 0.46f),
                        radius = w * 0.14f
                    ),
                    radius = w * 0.14f,
                    center = Offset(w * 0.62f, h * 0.46f)
                )
            }
        }

        // Outer glow ring
        if (showGlowRing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        BorderStroke(
                            2.dp,
                            Brush.sweepGradient(
                                listOf(
                                    glowColor.copy(alpha = 0.2f),
                                    glowColor.copy(alpha = 0.8f),
                                    glowColor.copy(alpha = 0.2f)
                                )
                            )
                        ),
                        containerShape
                    )
            )
        }
    }
}
