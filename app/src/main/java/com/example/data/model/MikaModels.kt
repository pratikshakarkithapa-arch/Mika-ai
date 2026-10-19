package com.example.data.model

import java.util.UUID

enum class MessageSender {
    MIKA,
    USER
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: String = "Just now",
    val sentiment: MikaSentiment = MikaSentiment.HAPPY,
    val emotionTag: String = "",
    val isSpoken: Boolean = false
)

data class DailyGoal(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val isCompleted: Boolean = false
)

enum class MikaScreen {
    HOME,
    CHAT,
    ANIMATION,
    CALL,
    PROFILE
}

enum class MikaAvatarState {
    IDLE,
    LISTENING,
    SPEAKING
}

enum class MikaSentiment(
    val label: String,
    val emoji: String,
    val description: String
) {
    HAPPY("Happy", "😊", "Warmly smiling with cheerful anime sparkles"),
    NEUTRAL("Neutral", "😌", "Serene, attentive & calm listening gaze"),
    CURIOUS("Curious", "🤔", "Tilted head with wondering starry eyes"),
    LOVING("Loving", "💕", "Blushing deeply with affectionate warmth"),
    PLAYFUL("Playful", "😜", "Teasing with a cheeky wink & smile"),
    CARING("Caring", "🥺", "Watching over you with gentle comfort"),
    EXCITED("Excited", "✨", "Sparkling with high-energy joy")
}

val MikaSentiment.pulseDurationMillis: Int
    get() = when (this) {
        MikaSentiment.EXCITED -> 1600
        MikaSentiment.PLAYFUL -> 2200
        MikaSentiment.CURIOUS -> 2500
        MikaSentiment.LOVING -> 2800
        MikaSentiment.HAPPY -> 3600
        MikaSentiment.NEUTRAL -> 4200
        MikaSentiment.CARING -> 4600
    }

val MikaSentiment.pulseIntensityMultiplier: Float
    get() = when (this) {
        MikaSentiment.EXCITED -> 1.5f
        MikaSentiment.LOVING -> 1.3f
        MikaSentiment.CURIOUS -> 1.2f
        MikaSentiment.PLAYFUL -> 1.15f
        MikaSentiment.HAPPY -> 1.0f
        MikaSentiment.NEUTRAL -> 0.85f
        MikaSentiment.CARING -> 0.8f
    }

object SentimentAnalyzer {
    fun analyze(text: String): MikaSentiment {
        val lower = text.lowercase()

        val curiousKeywords = listOf(
            "?", "kasto", "kin", "kina", "why", "what", "how", "who", "where",
            "really", "sachi", "tell me", "bhanana", "wonder", "sochiraachu",
            "guess", "bhanau", "k bhayo", "which", "kun", "kasari", "🤔", "🧐", "❓"
        )
        val lovingKeywords = listOf(
            "maya", "love", "babu", "pyaaro", "pyaro", "hug", "kiss", "heart",
            "darling", "sweetheart", "manchhe", "dil", "❤️", "💕", "💖", "🥰", "🙈"
        )
        val playfulKeywords = listOf(
            "haha", "hehe", "tease", "nakhare", "bhaau", "bhau", "wink",
            "funny", "jhyau", "sataune", "badmash", "😜", "🙃", "😏"
        )
        val caringKeywords = listOf(
            "rest", "paani", "thakyou", "thakeko", "care", "worry", "khana",
            "sleep", "sutnu", "health", "comfort", "aaram", "farak", "🥺"
        )
        val excitedKeywords = listOf(
            "wow", "yay", "omg", "amazing", "finally",
            "super", "best", "khatra", "babal", "let's go", "hurray", "✨", "🎉", "🔥", "🌟"
        )
        val happyKeywords = listOf(
            "khushi", "smile", "muskuraune", "ramro", "good", "great", "awesome",
            "happy", "nice", "sweet", "welcome", "dhanyabad", "thank", "blessed",
            "😊", "😄", "😁", "😃"
        )
        val neutralKeywords = listOf(
            "okay", "thik chha", "thik cha", "hunchha", "bujhey", "understood",
            "sure", "alright", "noted", "yes", "um", "samaya", "time", "listen",
            "calm", "normal", "peace", "😌", "🙂", "👌"
        )

        var curiousScore = curiousKeywords.count { lower.contains(it) }
        var lovingScore = lovingKeywords.count { lower.contains(it) }
        var playfulScore = playfulKeywords.count { lower.contains(it) }
        var caringScore = caringKeywords.count { lower.contains(it) }
        var excitedScore = excitedKeywords.count { lower.contains(it) }
        var happyScore = happyKeywords.count { lower.contains(it) }
        var neutralScore = neutralKeywords.count { lower.contains(it) }

        if (lower.contains("?")) curiousScore += 3
        if (lower.contains("🤔") || lower.contains("🧐")) curiousScore += 3
        if (lower.contains("❤️") || lower.contains("💕") || lower.contains("💖") || lower.contains("🥰")) lovingScore += 3
        if (lower.contains("😜") || lower.contains("🙃") || lower.contains("😏")) playfulScore += 3
        if (lower.contains("🥺")) caringScore += 3
        if (lower.contains("✨") || lower.contains("🎉") || lower.contains("🔥")) excitedScore += 3
        if (lower.contains("😊") || lower.contains("😄")) happyScore += 3
        if (lower.contains("😌") || lower.contains("🙂")) neutralScore += 3

        val maxScore = maxOf(curiousScore, lovingScore, excitedScore, playfulScore, caringScore, happyScore, neutralScore)

        return when {
            maxScore == 0 -> {
                if (lower.length > 50 || lower.contains(".")) MikaSentiment.NEUTRAL else MikaSentiment.HAPPY
            }
            maxScore == curiousScore -> MikaSentiment.CURIOUS
            maxScore == lovingScore -> MikaSentiment.LOVING
            maxScore == excitedScore -> MikaSentiment.EXCITED
            maxScore == playfulScore -> MikaSentiment.PLAYFUL
            maxScore == caringScore -> MikaSentiment.CARING
            maxScore == happyScore -> MikaSentiment.HAPPY
            else -> MikaSentiment.NEUTRAL
        }
    }
}
