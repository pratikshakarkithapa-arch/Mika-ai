package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gemini.GeminiService
import com.example.data.model.ChatMessage
import com.example.data.model.DailyGoal
import com.example.data.model.MessageSender
import com.example.data.model.MikaAvatarState
import com.example.data.model.MikaScreen
import com.example.data.model.MikaSentiment
import com.example.data.model.SentimentAnalyzer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class MikaUiState(
    val currentScreen: MikaScreen = MikaScreen.HOME,
    val avatarState: MikaAvatarState = MikaAvatarState.IDLE,
    val currentSentiment: MikaSentiment = MikaSentiment.LOVING,
    val chatMessages: List<ChatMessage> = emptyList(),
    val isSendingMessage: Boolean = false,
    val dailyGoals: List<DailyGoal> = emptyList(),
    val currentMood: String = "Feeling loved & playful 🥰",
    val moodTags: List<String> = listOf("Maya Garne", "Cute", "Teasing", "Caring"),
    val isCallActive: Boolean = false,
    val callDurationSeconds: Long = 765,
    val isCallMuted: Boolean = false,
    val isCallSpeakerOn: Boolean = true,
    val isFrontCamera: Boolean = true,
    val isApiKeyConfigured: Boolean = false,
    val animationPlaying: Boolean = true,
    val animationProgress: Float = 0f
)

class MikaViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiService = GeminiService()
    private val _uiState = MutableStateFlow(MikaUiState())
    val uiState: StateFlow<MikaUiState> = _uiState.asStateFlow()

    private var callTimerJob: Job? = null

    init {
        initDefaultData()
    }

    private fun initDefaultData() {
        val initialGoals = listOf(
            DailyGoal(title = "Paani piune / Drink water", isCompleted = true),
            DailyGoal(title = "Khana khane / Eat on time", isCompleted = true),
            DailyGoal(title = "Smile today / Muskuraune 😊", isCompleted = false),
            DailyGoal(title = "Chhitai sutne / Sleep early", isCompleted = true)
        )

        _uiState.update {
            it.copy(
                dailyGoals = initialGoals,
                isApiKeyConfigured = geminiService.isApiKeyConfigured()
            )
        }
    }

    fun setScreen(screen: MikaScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun toggleGoal(id: String) {
        _uiState.update { state ->
            val updated = state.dailyGoals.map {
                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
            }
            state.copy(dailyGoals = updated)
        }
    }

    fun addGoal(title: String) {
        if (title.isBlank()) return
        _uiState.update { state ->
            state.copy(dailyGoals = state.dailyGoals + DailyGoal(title = title.trim()))
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            sender = MessageSender.USER,
            text = text.trim()
        )

        val priorHistory = _uiState.value.chatMessages.takeLast(8).map {
            val s = if (it.sender == MessageSender.USER) "USER" else "MIKA"
            s to it.text
        }

        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isSendingMessage = true,
                avatarState = MikaAvatarState.LISTENING
            )
        }

        viewModelScope.launch {
            // Natural typing delay: feels like a real girl reading your message and typing back
            delay(Random.nextLong(1000L, 1600L))

            try {
                val replyText = geminiService.generateMikaReply(userMsg.text, priorHistory)
                val sentiment = SentimentAnalyzer.analyze(replyText)
                val emotionTag = when (sentiment) {
                    MikaSentiment.LOVING -> "Blushing 💕"
                    MikaSentiment.EXCITED -> "Sparkling ✨"
                    MikaSentiment.PLAYFUL -> "Teasing 😜"
                    MikaSentiment.CARING -> "Caring 🥺"
                    MikaSentiment.HAPPY -> "Smiling 😊"
                    MikaSentiment.CURIOUS -> "Curious 🤔"
                    MikaSentiment.NEUTRAL -> "Soft smile 😌"
                }

                val mikaMsg = ChatMessage(
                    sender = MessageSender.MIKA,
                    text = replyText,
                    sentiment = sentiment,
                    emotionTag = emotionTag
                )

                _uiState.update {
                    it.copy(
                        chatMessages = it.chatMessages + mikaMsg,
                        isSendingMessage = false,
                        currentSentiment = sentiment,
                        avatarState = MikaAvatarState.IDLE
                    )
                }
            } catch (e: Exception) {
                val fallbackText = geminiService.getOfflineMikaResponse(userMsg.text)
                val fallbackSentiment = SentimentAnalyzer.analyze(fallbackText)
                val mikaMsg = ChatMessage(
                    sender = MessageSender.MIKA,
                    text = fallbackText,
                    sentiment = fallbackSentiment,
                    emotionTag = "Loving 💕"
                )
                _uiState.update {
                    it.copy(
                        chatMessages = it.chatMessages + mikaMsg,
                        isSendingMessage = false,
                        currentSentiment = fallbackSentiment,
                        avatarState = MikaAvatarState.IDLE
                    )
                }
            }
        }
    }

    fun startCall() {
        _uiState.update { it.copy(isCallActive = true, currentScreen = MikaScreen.CALL) }
        startCallTimer()
    }

    fun endCall() {
        _uiState.update { it.copy(isCallActive = false, currentScreen = MikaScreen.HOME) }
        callTimerJob?.cancel()
    }

    fun toggleCallMute() {
        _uiState.update { it.copy(isCallMuted = !it.isCallMuted) }
    }

    fun toggleCallSpeaker() {
        _uiState.update { it.copy(isCallSpeakerOn = !it.isCallSpeakerOn) }
    }

    fun flipCamera() {
        _uiState.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }

    private fun startCallTimer() {
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (_uiState.value.isCallActive) {
                delay(1000)
                _uiState.update { it.copy(callDurationSeconds = it.callDurationSeconds + 1) }
            }
        }
    }

    fun setMood(mood: String, tags: List<String>) {
        _uiState.update { it.copy(currentMood = mood, moodTags = tags) }
    }

    fun clearChatMessages() {
        _uiState.update { it.copy(chatMessages = emptyList()) }
    }

    fun setSentiment(sentiment: MikaSentiment) {
        _uiState.update { it.copy(currentSentiment = sentiment) }
    }

    override fun onCleared() {
        super.onCleared()
        callTimerJob?.cancel()
    }
}
