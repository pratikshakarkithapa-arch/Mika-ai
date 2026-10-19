package com.example.ui.screens

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.R
import com.example.data.model.MikaScreen
import com.example.ui.theme.MikaDarkBg
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaSurfaceBorder
import com.example.ui.theme.MikaSurfaceDark
import com.example.ui.theme.MikaTextMuted
import com.example.ui.theme.MikaTextPrimary
import com.example.ui.theme.MikaTextSecondary
import com.example.viewmodel.MikaUiState
import com.example.viewmodel.MikaViewModel
import kotlinx.coroutines.delay

@Composable
fun AnimationScreen(
    uiState: MikaUiState,
    viewModel: MikaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videoUri = remember {
        Uri.parse("android.resource://${context.packageName}/${R.raw.mika_animation_8s}")
    }

    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var isPlaying by remember { mutableStateOf(true) }
    var isLooping by remember { mutableStateOf(true) }
    var currentPositionMs by remember { mutableIntStateOf(0) }
    var currentStage by remember { mutableStateOf("0-2s: First gentle smile") }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(100)
            val pos = videoViewRef?.currentPosition ?: 0
            currentPositionMs = pos

            val sec = pos / 1000f
            currentStage = when {
                sec < 2.0f -> "0–2s: Notices viewer, eye contact & gentle smile 💕"
                sec < 4.0f -> "2–4s: Resetting to calm relaxed pose 😌"
                sec < 6.0f -> "4–6s: Second time noticing with warm shy smile 🥰"
                else -> "6–8s: Sweeter closed-eye heartwarming smile ✨"
            }

            if (pos >= 7900 && !isLooping) {
                isPlaying = false
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // Pure solid white background requested by user
    ) {
        // Video Player Container (Vertical 9:16)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.setScreen(MikaScreen.HOME) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .testTag("anim_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Mika 2D Animation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "8-Second High Quality • 9:16",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                IconButton(
                    onClick = { isLooping = !isLooping },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isLooping) Color(0xFFEDE9FE) else Color(0xFFF1F5F9))
                        .testTag("anim_loop_toggle")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Repeat,
                        contentDescription = "Loop Animation",
                        tint = if (isLooping) MikaNeonPurple else Color(0xFF64748B)
                    )
                }
            }

            // Video Player
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(videoUri)
                            setOnPreparedListener { mp ->
                                mp.isLooping = isLooping
                                mp.setVolume(0f, 0f) // Silent video
                                start()
                                isPlaying = true
                            }
                            setOnCompletionListener {
                                if (isLooping) {
                                    start()
                                } else {
                                    isPlaying = false
                                }
                            }
                            videoViewRef = this
                        }
                    },
                    update = { view ->
                        videoViewRef = view
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                )
            }

            // Timeline Checkpoint Card & Playback Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Current Stage Indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentStage,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MikaNeonPurple
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar
                val progress = (currentPositionMs / 8000f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MikaNeonPurple,
                    trackColor = Color(0xFFE2E8F0)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Playback Control Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Replay button
                    IconButton(
                        onClick = {
                            videoViewRef?.seekTo(0)
                            videoViewRef?.start()
                            isPlaying = true
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Replay,
                            contentDescription = "Replay",
                            tint = Color(0xFF1E293B)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Play/Pause button
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(MikaNeonPurple)
                            .clickable {
                                videoViewRef?.let { vv ->
                                    if (vv.isPlaying) {
                                        vv.pause()
                                        isPlaying = false
                                    } else {
                                        vv.start()
                                        isPlaying = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Time display
                    Text(
                        text = String.format("%.1fs / 8.0s", currentPositionMs / 1000f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            videoViewRef?.stopPlayback()
        }
    }
}
