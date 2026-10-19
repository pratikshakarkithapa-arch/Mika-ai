package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.data.model.MikaSentiment

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Mika Theme Colors
val MikaDarkBg = Color(0xFF0D0818)
val MikaDeepPurple = Color(0xFF19102C)
val MikaSurfaceDark = Color(0xFF140D24)
val MikaSurfaceElevated = Color(0xFF201538)
val MikaSurfaceBorder = Color(0xFF38245C)

val MikaNeonPurple = Color(0xFFA855F7)
val MikaNeonPurpleLight = Color(0xFFC084FC)
val MikaVibrantPink = Color(0xFFEC4899)
val MikaGreenOnline = Color(0xFF10B981)

val MikaAiBubble = Color(0xFF22163E)
val MikaAiBubbleBorder = Color(0xFF4A327E)
val MikaUserBubbleStart = Color(0xFF9333EA)
val MikaUserBubbleEnd = Color(0xFFC026D3)

val MikaTextPrimary = Color(0xFFF8FAFC)
val MikaTextSecondary = Color(0xFFCBD5E1)
val MikaTextMuted = Color(0xFF94A3B8)

val MikaCallEndRed = Color(0xFFEF4444)
val MikaCallBtnBg = Color(0xFF22173B)

fun getSentimentGlowColor(sentiment: MikaSentiment): Color {
    return when (sentiment) {
        MikaSentiment.EXCITED -> MikaVibrantPink
        MikaSentiment.LOVING -> Color(0xFFF472B6)
        MikaSentiment.PLAYFUL -> MikaNeonPurpleLight
        MikaSentiment.HAPPY -> MikaNeonPurple
        MikaSentiment.CURIOUS -> Color(0xFF818CF8)
        MikaSentiment.CARING -> Color(0xFFA78BFA)
        MikaSentiment.NEUTRAL -> MikaDeepPurple
    }
}
