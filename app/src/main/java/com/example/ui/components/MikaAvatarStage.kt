package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MikaAvatarState
import com.example.data.model.MikaSentiment
import com.example.data.model.pulseDurationMillis
import com.example.ui.theme.MikaDarkBg
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaSurfaceBorder
import com.example.ui.theme.MikaSurfaceElevated
import com.example.ui.theme.MikaTextPrimary
import com.example.ui.theme.MikaTextSecondary
import com.example.ui.theme.getSentimentGlowColor

/**
 * MikaAvatarStage provides the animated anime character viewport for Mika AI,
 * accompanied by a state-based UI overlay reflecting Mika's expression
 * (e.g., Happy, Neutral, Curious, etc.) analyzed from the Gemini API.
 */
@Composable
fun MikaAvatarStage(
    modifier: Modifier = Modifier,
    avatarState: MikaAvatarState = MikaAvatarState.IDLE,
    sentiment: MikaSentiment = MikaSentiment.HAPPY,
    stageSize: Dp = 280.dp,
    showGlowRing: Boolean = true,
    showExpressionOverlay: Boolean = true,
    roundedCorners: Dp = 28.dp,
    isCircle: Boolean = false,
    useFullScene: Boolean = true,
    onAvatarClick: (() -> Unit)? = null,
    onSentimentSelect: ((MikaSentiment) -> Unit)? = null
) {
    var isSelectorExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        // Character Canvas Layer with dynamic living animation & facial expression overlay
        CanvasMikaCharacter(
            modifier = Modifier.fillMaxSize(),
            avatarState = avatarState,
            sentiment = sentiment,
            stageSize = stageSize,
            showGlowRing = showGlowRing,
            roundedCorners = roundedCorners,
            isCircle = isCircle,
            onCharacterClick = onAvatarClick
        )

        // Floating State-Based UI Expression Overlay
        if (showExpressionOverlay && !isCircle) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("mika_expression_overlay"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MikaExpressionPill(
                    sentiment = sentiment,
                    isExpanded = isSelectorExpanded,
                    onToggleExpand = { isSelectorExpanded = !isSelectorExpanded }
                )

                // Optional interactive quick-preview drawer for testing/previewing expressions
                AnimatedVisibility(
                    visible = isSelectorExpanded,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MikaDarkBg.copy(alpha = 0.92f))
                            .border(1.dp, MikaSurfaceBorder, RoundedCornerShape(20.dp))
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MikaSentiment.values().forEach { stateOption ->
                            val isSelected = stateOption == sentiment
                            val chipGlow = getSentimentGlowColor(stateOption)

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) chipGlow.copy(alpha = 0.25f) else Color.Transparent
                                    )
                                    .border(
                                        BorderStroke(
                                            1.dp,
                                            if (isSelected) chipGlow else MikaSurfaceBorder.copy(alpha = 0.5f)
                                        ),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        onSentimentSelect?.invoke(stateOption)
                                        isSelectorExpanded = false
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("expression_chip_${stateOption.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = stateOption.emoji,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stateOption.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MikaNeonPurpleLight else MikaTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * State-based floating pill badge displaying the detected sentiment & emotional expression.
 */
@Composable
private fun MikaExpressionPill(
    sentiment: MikaSentiment,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val glowColor = getSentimentGlowColor(sentiment)
    val infiniteTransition = rememberInfiniteTransition(label = "PulsePill_${sentiment.name}")
    val pulseFraction by infiniteTransition.animateFloat(
        initialValue = 0.40f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = sentiment.pulseDurationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pillPulse"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MikaDarkBg.copy(alpha = 0.88f))
            .border(
                BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        listOf(
                            glowColor.copy(alpha = pulseFraction),
                            MikaSurfaceBorder,
                            glowColor.copy(alpha = pulseFraction * 0.5f)
                        )
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .clickable { onToggleExpand() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        AnimatedContent(
            targetState = sentiment,
            transitionSpec = {
                (slideInVertically { height -> -height } + fadeIn()) togetherWith
                        (slideOutVertically { height -> height } + fadeOut())
            },
            label = "ExpressionPillContent"
        ) { targetSentiment ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Glowing emotional state indicator dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(getSentimentGlowColor(targetSentiment).copy(alpha = pulseFraction))
                )

                Spacer(modifier = Modifier.width(7.dp))

                // Sentiment Emoji
                Text(
                    text = targetSentiment.emoji,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Expression Title
                Text(
                    text = "Expression: ${targetSentiment.label}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MikaTextPrimary
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Small expand affordance
                Text(
                    text = if (isExpanded) "▲" else "▼",
                    fontSize = 9.sp,
                    color = MikaNeonPurpleLight.copy(alpha = 0.8f)
                )
            }
        }
    }
}

