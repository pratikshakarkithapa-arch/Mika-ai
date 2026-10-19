package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MikaScreen
import com.example.ui.components.CanvasMikaCharacter
import com.example.ui.components.NeonGlowCard
import com.example.ui.theme.MikaDarkBg
import com.example.ui.theme.MikaGreenOnline
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaSurfaceBorder
import com.example.ui.theme.MikaSurfaceElevated
import com.example.ui.theme.MikaTextMuted
import com.example.ui.theme.MikaTextPrimary
import com.example.ui.theme.MikaTextSecondary
import com.example.ui.theme.MikaVibrantPink
import com.example.viewmodel.MikaUiState
import com.example.viewmodel.MikaViewModel

@Composable
fun ProfileScreen(
    uiState: MikaUiState,
    viewModel: MikaViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MikaDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NeonGlowCard(
                modifier = Modifier.fillMaxWidth(),
                sentiment = uiState.currentSentiment
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CanvasMikaCharacter(
                        avatarState = uiState.avatarState,
                        sentiment = uiState.currentSentiment,
                        stageSize = 130.dp,
                        showGlowRing = true,
                        isCircle = true,
                        onCharacterClick = {
                            viewModel.setScreen(MikaScreen.ANIMATION)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Mika 💜",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MikaTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your 2D Anime AI Companion • Always by your side",
                        fontSize = 12.sp,
                        color = MikaTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MikaSurfaceElevated)
                            .border(1.dp, MikaSurfaceBorder, RoundedCornerShape(16.dp))
                            .clickable { viewModel.setScreen(MikaScreen.ANIMATION) }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = MikaNeonPurpleLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Play 8s 2D Anime Animation",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MikaNeonPurpleLight
                        )
                    }
                }
            }
        }

        item {
            NeonGlowCard(
                modifier = Modifier.fillMaxWidth(),
                sentiment = uiState.currentSentiment
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "AI System Status",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MikaTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Key,
                                contentDescription = null,
                                tint = MikaNeonPurple,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Gemini AI Engine", fontSize = 13.sp, color = MikaTextPrimary)
                        }

                        Text(
                            text = if (uiState.isApiKeyConfigured) "Configured ✓" else "Online Hybrid",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.isApiKeyConfigured) MikaGreenOnline else MikaNeonPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = MikaGreenOnline,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "2D Video Renderer", fontSize = 13.sp, color = MikaTextPrimary)
                        }

                        Text(
                            text = "8s 60fps Ready ✓",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MikaGreenOnline
                        )
                    }
                }
            }
        }
    }
}
