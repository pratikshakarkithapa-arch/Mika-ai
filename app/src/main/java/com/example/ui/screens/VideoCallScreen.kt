package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MikaAvatarState
import com.example.ui.components.AudioWaveform
import com.example.ui.components.CameraPreviewView
import com.example.ui.components.MikaAvatarStage
import com.example.ui.theme.MikaCallBtnBg
import com.example.ui.theme.MikaCallEndRed
import com.example.ui.theme.MikaDarkBg
import com.example.ui.theme.MikaGreenOnline
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaSurfaceBorder
import com.example.ui.theme.MikaSurfaceDark
import com.example.ui.theme.MikaTextPrimary
import com.example.ui.theme.MikaTextSecondary
import com.example.viewmodel.MikaUiState
import com.example.viewmodel.MikaViewModel

@Composable
fun VideoCallScreen(
    uiState: MikaUiState,
    viewModel: MikaViewModel,
    hasCameraPermission: Boolean = true,
    modifier: Modifier = Modifier
) {
    val durationText = formatDuration(uiState.callDurationSeconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MikaDarkBg)
    ) {
        // Fullscreen Mika Character View (matching Image 1 video call layout)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Fullscreen animated avatar stage showing Mika in her purple bedroom
            MikaAvatarStage(
                avatarState = uiState.avatarState,
                sentiment = uiState.currentSentiment,
                stageSize = 0.dp,
                roundedCorners = 0.dp,
                showGlowRing = false,
                modifier = Modifier.fillMaxSize()
            )

            // Neon Waveform across Mika's chest when speaking or listening
            if (uiState.avatarState == MikaAvatarState.SPEAKING || uiState.avatarState == MikaAvatarState.LISTENING) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 180.dp)
                ) {
                    AudioWaveform(isActive = true)
                }
            }
        }

        // Top Call Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, MikaNeonPurple, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mika_avatar),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Mika AI",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MikaTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "💜", fontSize = 13.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(MikaGreenOnline)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = durationText,
                            fontSize = 12.sp,
                            color = MikaTextSecondary
                        )
                    }
                }
            }

            // Top camera switch button
            IconButton(
                onClick = { viewModel.flipCamera() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MikaCallBtnBg.copy(alpha = 0.8f))
                    .border(1.dp, MikaSurfaceBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Flip Camera",
                    tint = MikaNeonPurpleLight
                )
            }
        }

        // Picture-in-Picture (PiP) user camera preview window (Bottom Right)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-20).dp, y = (-140).dp)
                .size(width = 110.dp, height = 150.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, MikaNeonPurple.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                .testTag("pip_camera_preview")
        ) {
            CameraPreviewView(
                modifier = Modifier.fillMaxSize(),
                isFrontCamera = uiState.isFrontCamera,
                hasCameraPermission = hasCameraPermission
            )

            // Small switch icon in PiP corner
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MikaDarkBg.copy(alpha = 0.7f))
                    .clickable { viewModel.flipCamera() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Flip",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Bottom Call Action Bar matching Image 1
        // [ Flip ]  [ Mute ]  [ (End) ]  [ Speaker ]  [ More ]
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(MikaSurfaceDark.copy(alpha = 0.92f))
                    .border(1.dp, MikaSurfaceBorder, RoundedCornerShape(32.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flip
                CallControlButton(
                    label = "Flip",
                    icon = Icons.Default.FlipCameraAndroid,
                    isActive = false,
                    onClick = { viewModel.flipCamera() },
                    testTag = "call_btn_flip"
                )

                // Mute
                CallControlButton(
                    label = if (uiState.isCallMuted) "Unmute" else "Mute",
                    icon = if (uiState.isCallMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    isActive = uiState.isCallMuted,
                    onClick = { viewModel.toggleCallMute() },
                    testTag = "call_btn_mute"
                )

                // End Call (Prominent Red Button)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { viewModel.endCall() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(MikaCallEndRed)
                            .testTag("call_btn_end"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "End Call",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = "End",
                        fontSize = 11.sp,
                        color = MikaTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Speaker
                CallControlButton(
                    label = "Speaker",
                    icon = if (uiState.isCallSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                    isActive = uiState.isCallSpeakerOn,
                    onClick = { viewModel.toggleCallSpeaker() },
                    testTag = "call_btn_speaker"
                )

                // More
                CallControlButton(
                    label = "More",
                    icon = Icons.Default.MoreHoriz,
                    isActive = false,
                    onClick = {
                        viewModel.sendMessage("I'm so glad we're on a call together, Mika! 💜")
                    },
                    testTag = "call_btn_more"
                )
            }
        }
    }
}

@Composable
private fun CallControlButton(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(if (isActive) MikaNeonPurple.copy(alpha = 0.35f) else MikaCallBtnBg)
                .border(
                    1.dp,
                    if (isActive) MikaNeonPurple else MikaSurfaceBorder,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) MikaNeonPurpleLight else Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = MikaTextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private fun formatDuration(seconds: Long): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hrs, mins, secs)
}
