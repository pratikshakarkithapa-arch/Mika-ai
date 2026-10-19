package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MikaScreen
import com.example.ui.theme.MikaDarkBg
import com.example.ui.theme.MikaDeepPurple
import com.example.ui.theme.MikaNeonPurple
import com.example.ui.theme.MikaNeonPurpleLight
import com.example.ui.theme.MikaSurfaceBorder
import com.example.ui.theme.MikaSurfaceDark
import com.example.ui.theme.MikaTextMuted
import com.example.ui.theme.MikaVibrantPink

@Composable
fun BottomNavBar(
    currentScreen: MikaScreen,
    onScreenSelected: (MikaScreen) -> Unit,
    onCenterLogoClick: () -> Unit,
    isListening: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CenterPulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MikaSurfaceDark)
                .border(
                    BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(
                                MikaSurfaceBorder,
                                MikaNeonPurple.copy(alpha = 0.35f),
                                MikaSurfaceBorder
                            )
                        )
                    ),
                    RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavBarItem(
                    label = "Home",
                    icon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    isSelected = currentScreen == MikaScreen.HOME,
                    onClick = { onScreenSelected(MikaScreen.HOME) },
                    testTag = "nav_home"
                )

                // 2D Anime Animation
                NavBarItem(
                    label = "2D Anime",
                    icon = Icons.Filled.AutoAwesome,
                    unselectedIcon = Icons.Outlined.AutoAwesome,
                    isSelected = currentScreen == MikaScreen.ANIMATION,
                    onClick = { onScreenSelected(MikaScreen.ANIMATION) },
                    testTag = "nav_animation"
                )

                // Center Chat Action FAB
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(MikaNeonPurple, MikaVibrantPink)
                            )
                        )
                        .clickable { onCenterLogoClick() }
                        .testTag("nav_chat_center"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChatBubble,
                        contentDescription = "Chat with Mika",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Chat Tab
                NavBarItem(
                    label = "Chat",
                    icon = Icons.Filled.ChatBubble,
                    unselectedIcon = Icons.Outlined.ChatBubbleOutline,
                    isSelected = currentScreen == MikaScreen.CHAT,
                    onClick = { onScreenSelected(MikaScreen.CHAT) },
                    testTag = "nav_chat"
                )

                // Profile
                NavBarItem(
                    label = "Profile",
                    icon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.PersonOutline,
                    isSelected = currentScreen == MikaScreen.PROFILE,
                    onClick = { onScreenSelected(MikaScreen.PROFILE) },
                    testTag = "nav_profile"
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    icon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) icon else unselectedIcon,
            contentDescription = label,
            tint = if (isSelected) MikaNeonPurpleLight else MikaTextMuted,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MikaNeonPurpleLight else MikaTextMuted
        )
    }
}
