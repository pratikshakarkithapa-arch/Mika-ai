package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class VoiceSpeechManager(
    private val context: Context,
    private val onTtsStateChanged: (Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val nepaliLocale = Locale("ne", "NP")
            val nepaliResult = tts?.isLanguageAvailable(nepaliLocale)
            if (nepaliResult == TextToSpeech.LANG_AVAILABLE || nepaliResult == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                tts?.setLanguage(nepaliLocale)
            } else {
                // Default to US English which smoothly pronounces Romanized Nepali
                tts?.setLanguage(Locale.US)
            }

            isTtsReady = true
            tts?.setPitch(1.24f) // Cute sweet anime girl pitch
            tts?.setSpeechRate(1.02f)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    onTtsStateChanged(true)
                }

                override fun onDone(utteranceId: String?) {
                    onTtsStateChanged(false)
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    onTtsStateChanged(false)
                }
            })
        }
    }

    fun speak(text: String) {
        if (!isTtsReady) return
        val cleaned = text
            .replace(Regex("[*#_`~]"), "")
            .replace(Regex("[\\p{So}\\p{Cn}]"), " ")
            .trim()
        val utteranceId = "MikaSpeech_${System.currentTimeMillis()}"
        tts?.speak(cleaned, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stopSpeaking() {
        tts?.stop()
        onTtsStateChanged(false)
    }

    fun setPitch(pitch: Float) {
        tts?.setPitch(pitch)
    }

    fun setSpeechRate(rate: Float) {
        tts?.setSpeechRate(rate)
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
    }
}
