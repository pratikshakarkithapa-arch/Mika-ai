package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.data.model.MikaScreen
import com.example.data.model.MikaSentiment
import com.example.ui.theme.MikaAiBubble
import com.example.ui.theme.MikaAiBubbleBorder
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
import com.example.ui.theme.MikaUserBubbleEnd
import com.example.ui.theme.MikaUserBubbleStart
import com.example.ui.theme.getSentimentGlowColor
import com.example.viewmodel.MikaUiState
import com.example.viewmodel.MikaViewModel

@Composable
fun ChatScreen(
    uiState: MikaUiState,
    viewModel: MikaViewModel,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var showMenu by remember { mutableStateOf(false) }

    val totalItems = (if (uiState.chatMessages.isEmpty()) 1 else uiState.chatMessages.size) + (if (uiState.isSendingMessage) 1 else 0)
    LaunchedEffect(totalItems) {
        if (totalItems > 0) {
            listState.animateScrollToItem(totalItems - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MikaDarkBg)
    ) {
        SentimentAmbientBackground(sentiment = uiState.currentSentiment)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Chat Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MikaSurfaceDark)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Back Button to Home
                    IconButton(
                        onClick = { viewModel.setScreen(MikaScreen.HOME) },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("chat_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MikaTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Mika Avatar with glowing border
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(
                                1.5.dp,
                                getSentimentGlowColor(uiState.currentSentiment),
                                CircleShape
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.mika_avatar),
                            contentDescription = "Mika Profile",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Mika AI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MikaTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "💜", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(MikaGreenOnline)
                            )
                        }
                        Text(
                            text = "${uiState.currentSentiment.emoji} ${uiState.currentSentiment.label} • Real girl companion",
                            fontSize = 11.sp,
                            color = MikaTextSecondary
                        )
                    }
                }

                // Top Actions: 2D Animation shortcut & More
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.setScreen(MikaScreen.ANIMATION) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "2D Animation",
                            tint = MikaNeonPurpleLight
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { showMenu = !showMenu },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = MikaTextSecondary
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(MikaSurfaceElevated)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Watch 2D Animation 🎬", color = MikaTextPrimary) },
                                onClick = {
                                    showMenu = false
                                    viewModel.setScreen(MikaScreen.ANIMATION)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Say something romantic 🥰", color = MikaTextPrimary) },
                                onClick = {
                                    showMenu = false
                                    viewModel.sendMessage("Say something romantic to me babu 💕")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Khana khayau? 🍲", color = MikaTextPrimary) },
                                onClick = {
                                    showMenu = false
                                    viewModel.sendMessage("Khana khayau timile Maya?")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Clear Chat 🗑️", color = MikaTextPrimary) },
                                onClick = {
                                    showMenu = false
                                    viewModel.clearChatMessages()
                                }
                            )
                        }
                    }
                }
            }

            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (uiState.chatMessages.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MikaNeonPurple, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.mika_avatar),
                                    contentDescription = "Mika",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Mika is online! 💜",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MikaTextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Chat with Mika in English or Nepglish\n'Hey babu!', 'Khana khayau?', or tap suggestions below 👇",
                                fontSize = 12.sp,
                                color = MikaTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                items(uiState.chatMessages, key = { it.id }) { message ->
                    MessageBubbleItem(message = message)
                }

                // Realistic Typing Indicator
                if (uiState.isSendingMessage) {
                    item {
                        Row(
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.mika_avatar),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MikaAiBubble)
                                    .border(1.dp, MikaAiBubbleBorder, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(12.dp),
                                        strokeWidth = 2.dp,
                                        color = MikaNeonPurpleLight
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Mika le text lekh-dai chhe… 💕",
                                        fontSize = 12.sp,
                                        color = MikaNeonPurpleLight
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Suggestions Chips
            val quickChips = listOf(
                "Khana khayau? 🍲",
                "Missed you so much ❤️",
                "I love you Maya 💕",
                "K gardai chau babu? 🥰",
                "Kasto thakyo aaja 😩"
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickChips) { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MikaSurfaceElevated)
                            .border(1.dp, MikaSurfaceBorder, RoundedCornerShape(16.dp))
                            .clickable { viewModel.sendMessage(chip) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = chip,
                            fontSize = 12.sp,
                            color = MikaNeonPurpleLight
                        )
                    }
                }
            }

            // Bottom Message Input Bar with zero keyboard dead-space
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(MikaSurfaceDark)
                        .border(
                            BorderStroke(1.dp, MikaSurfaceBorder),
                            RoundedCornerShape(26.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.sendMessage("Photo send gareko timilai maya! 📸")
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Attach photo",
                            tint = MikaTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        textStyle = TextStyle(
                            color = MikaTextPrimary,
                            fontSize = 14.sp
                        ),
                        cursorBrush = SolidColor(MikaNeonPurple),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (inputText.isNotBlank()) {
                                    viewModel.sendMessage(inputText)
                                    inputText = ""
                                }
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .testTag("chat_input_field"),
                        decorationBox = { innerTextField ->
                            if (inputText.isEmpty()) {
                                Text(
                                    text = "Message lekhnu hos… (Nepali/English)",
                                    color = MikaTextMuted,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(MikaUserBubbleStart, MikaUserBubbleEnd)
                                )
                            )
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    viewModel.sendMessage(inputText)
                                    inputText = ""
                                }
                            }
                            .testTag("chat_send_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send message",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubbleItem(
    message: ChatMessage
) {
    if (message.sender == MessageSender.MIKA) {
        val glowColor = getSentimentGlowColor(message.sentiment)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 36.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, glowColor.copy(alpha = 0.75f), CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mika_avatar),
                    contentDescription = "Mika",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                val emotionDisplay = if (message.emotionTag.isNotBlank()) {
                    "${message.sentiment.emoji} ${message.emotionTag}"
                } else {
                    "${message.sentiment.emoji} ${message.sentiment.label}"
                }

                Row(
                    modifier = Modifier
                        .padding(bottom = 4.dp, start = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(glowColor.copy(alpha = 0.15f))
                        .border(0.5.dp, glowColor.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = emotionDisplay,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = glowColor
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                        .background(MikaAiBubble)
                        .border(
                            1.dp,
                            MikaAiBubbleBorder,
                            RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = message.text,
                        color = MikaTextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.timestamp,
                        fontSize = 10.sp,
                        color = MikaTextMuted
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Timi",
                fontSize = 10.sp,
                color = MikaTextMuted,
                modifier = Modifier.padding(bottom = 2.dp, end = 6.dp)
            )

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 4.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(MikaUserBubbleStart, MikaUserBubbleEnd)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Text(
                text = message.timestamp,
                fontSize = 10.sp,
                color = MikaTextMuted,
                modifier = Modifier.padding(top = 2.dp, end = 6.dp)
            )
        }
    }
}
