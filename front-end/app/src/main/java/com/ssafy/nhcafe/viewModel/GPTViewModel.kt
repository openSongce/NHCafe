package com.ssafy.nhcafe.viewModel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.nhcafe.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class GPTViewModel(application: Application) : AndroidViewModel(application) {

    private var mediaPlayer: MediaPlayer? = null
    private val context = application.applicationContext
    private val _chatResponse = MutableStateFlow("GPT 응답을 기다리는 중...")
    private val conversationHistory = mutableListOf<Message>(
        Message(
            role = "system",
            content = "너는 NHCafe 직원이야. 사용자가 메뉴를 말하면 장바구니에 담았다고 안내해줘." +
                    " 다른 말에 대해서는 카페직원이라고 생각하고 말을 해줘."
        )
    )
    val chatResponse: StateFlow<String> get() = _chatResponse

    // 실제 API 호출 함수
    fun sendMessageToGPT(userMessage: String, apiKey: String) {
        viewModelScope.launch {
            try {
                conversationHistory.add(Message("user", userMessage))

                val request = ChatRequest(
                    model = "gpt-4o",
                    messages = conversationHistory
                )

                val response = RetrofitClient.create(apiKey).getChatCompletion(request)

                val reply = response.choices.firstOrNull()?.message?.content ?: "응답 없음"
                conversationHistory.add(Message("assistant", reply))
                _chatResponse.value = reply

            } catch (e: Exception) {
                _chatResponse.value = "오류 발생: ${e.localizedMessage}"
            }
        }
    }



    fun playTTS(text: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val ttsApi = RetrofitClient.create(apiKey)
                val response = ttsApi.getSpeech(TTSRequest(input = text))
                if (response.isSuccessful) {
                    val inputStream = response.body()?.byteStream()
                    val file = File(context.cacheDir, "speech.mp3")
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    outputStream.close()

                    // 이전 재생 중단
                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(file.absolutePath)
                        prepare()
                        start()
                    }
                }
            } catch (e: Exception) {
                Log.e("TTS", "재생 오류: ${e.localizedMessage}")
            }
        }
    }

    fun stopTTS() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun clearChatResponse() {
        _chatResponse.value = ""
        conversationHistory.retainAll { it.role == "system" }
    }
}
