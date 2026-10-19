package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MikaScreen
import com.example.data.model.MikaSentiment
import com.example.data.model.pulseDurationMillis
import com.example.data.model.pulseIntensityMultiplier
import com.example.ui.components.CanvasMikaCharacter
import com.example.ui.components.NeonGlowCard
import com.example.ui.theme.MikaDarkBg
import com.example.ui.theme.MikaGreenOnline
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaSurfaceBorder
import com.example.ui.theme.MikaSurfaceDark
import com.example.ui.theme.MikaSurfaceElevated
import com.example.ui.theme.MikaTextMuted
import com.example.ui.theme.MikaTextPrimary
import com.example.ui.theme.MikaTextSecondary
import com.example.ui.theme.MikaVibrantPink
import com.example.ui.theme.getSentimentGlowColor
import com.example.viewmodel.MikaUiState
import com.example.viewmodel.MikaViewModel

@Composable
fun HomeScreen(
    uiState: MikaUiState,
    viewModel: MikaViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MikaDarkBg)
    ) {
        SentimentAmbientBackground(sentiment = uiState.currentSentiment)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Mika AI",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MikaTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "💜", fontSize = 20.sp)
                        }
                        Text(
                            text = "Your Expressive 2D Anime Companion",
                            fontSize = 12.sp,
                            color = MikaTextSecondary
                        )
                    }

                    // Online pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MikaSurfaceElevated)
                            .border(1.dp, MikaSurfaceBorder, RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MikaGreenOnline)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Online",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MikaGreenOnline
                            )
                        }
                    }
                }
            }

            // Mika Anime Character Stage Card
            item {
                NeonGlowCard(
                    modifier = Modifier.fillMaxWidth(),
                    sentiment = uiState.currentSentiment
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CanvasMikaCharacter(
                            avatarState = uiState.avatarState,
                            sentiment = uiState.currentSentiment,
                            stageSize = 240.dp,
                            showGlowRing = true,
                            roundedCorners = 20.dp,
                            onCharacterClick = {
                                viewModel.setScreen(MikaScreen.ANIMATION)
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Current Emotion Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MikaSurfaceElevated)
                                .border(1.dp, MikaSurfaceBorder, RoundedCornerShape(16.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${uiState.currentSentiment.emoji} Expression: ${uiState.currentSentiment.label}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MikaNeonPurpleLight
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = uiState.currentSentiment.description,
                            fontSize = 12.sp,
                            color = MikaTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // Featured 8-Second 2D Anime Animation Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF2E1065), Color(0xFF4C1D95))
                            )
                        )
                        .border(
                            1.dp,
                            MikaNeonPurpleLight.copy(alpha = 0.5f),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.setScreen(MikaScreen.ANIMATION) }
                        .padding(16.dp)
                        .testTag("card_featured_animation")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🎬 8-Second 2D Animation",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "High-quality 2D anime animation • Pure white background • Two gentle noticing & smiling moments",
                                fontSize = 12.sp,
                                color = Color(0xFFDDD6FE)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(MikaNeonPurpleLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayCircle,
                                contentDescription = "Watch Animation",
                                tint = Color(0xFF2E1065),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.setScreen(MikaScreen.CHAT) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("home_chat_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MikaNeonPurple
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ChatBubble,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Chat with Mika", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.setScreen(MikaScreen.ANIMATION) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("home_anim_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MikaSurfaceElevated
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MikaSurfaceBorder),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = MikaNeonPurpleLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Watch 2D Video", color = MikaTextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Daily Check-ins
            item {
                Text(
                    text = "Daily Goals with Mika 💕",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MikaTextPrimary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(uiState.dailyGoals.size) { index ->
                val goal = uiState.dailyGoals[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MikaSurfaceDark)
                        .border(1.dp, MikaSurfaceBorder, RoundedCornerShape(14.dp))
                        .clickable { viewModel.toggleGoal(goal.id) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (goal.isCompleted) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (goal.isCompleted) MikaNeonPurpleLight else MikaTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = goal.title,
                        fontSize = 13.sp,
                        color = if (goal.isCompleted) MikaTextMuted else MikaTextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun SentimentAmbientBackground(
    sentiment: MikaSentiment,
    modifier: Modifier = Modifier
) {
    val duration = sentiment.pulseDurationMillis
    val intensity = sentiment.pulseIntensityMultiplier

    val infiniteTransition = rememberInfiniteTransition(label = "BgPulse")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0.3f * intensity,
        targetValue = 0.9f * intensity.coerceAtMost(1.2f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseProgress"
    )

    val glowColor = getSentimentGlowColor(sentiment)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    glowColor.copy(alpha = 0.15f * pulseProgress),
                    Color.Transparent
                ),
                center = Offset(w * 0.25f, h * 0.2f),
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = Offset(w * 0.25f, h * 0.2f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    MikaNeonPurple.copy(alpha = 0.12f * pulseProgress),
                    Color.Transparent
                ),
                center = Offset(w * 0.8f, h * 0.65f),
                radius = w * 0.65f
            ),
            radius = w * 0.65f,
            center = Offset(w * 0.8f, h * 0.65f)
        )
    }
}
