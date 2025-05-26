package com.ssafy.nhcafe.viewModel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    val prompt = """
카페의 추천 메뉴 3가지를 JSON 배열 형태로 반환해주세요.
절대로 다른 문장이나 설명을 넣지 말고, 오직 JSON만 반환해주세요.
image는 latte, americano, cappuccino 셋 중에 하나 랜덤으로
그냥 말로만 해줘
예:
[
  {"name": "카페라떼", "description": "부드러운 우유의 풍미", "image": "latte"},
  {"name": "아메리카노", "description": "진한 에스프레소의 깊은 맛", "image": "americano"},
  {"name": "카푸치노", "description": "풍부한 거품과 진한 맛", "image": "cappuccino"}
]
""".trimIndent()

    fun getLocalizedPrompt(isKorean: Boolean): String {
        return if (isKorean) {
            """
            카페의 추천 메뉴 3가지를 JSON 배열 형태로 반환해주세요.
            절대로 다른 문장이나 설명을 넣지 말고, 오직 JSON만 반환해주세요.
            image는 latte, americano, cappuccino 셋 중에 하나 랜덤으로
            그냥 말로만 해줘
            예:
            [
              {"name": "카페라떼", "description": "부드러운 우유의 풍미", "image": "latte"},
              {"name": "아메리카노", "description": "진한 에스프레소의 깊은 맛", "image": "americano"},
              {"name": "카푸치노", "description": "풍부한 거품과 진한 맛", "image": "cappuccino"}
            ]
        """.trimIndent()
        } else {
            """
            카페의 추천 메뉴 3가지를 JSON 배열 형태로 반환해주세요.
            절대로 다른 문장이나 설명을 넣지 말고, 오직 JSON만 반환해주세요.
            image는 latte, americano, cappuccino 셋 중에 하나 랜덤으로
            name이랑 description을 영어로 해줘.
            그냥 말로만 해줘. 
            예:
            [
              {"name": "카페라떼", "description": "부드러운 우유의 풍미", "image": "latte"},
              {"name": "아메리카노", "description": "진한 에스프레소의 깊은 맛", "image": "americano"},
              {"name": "카푸치노", "description": "풍부한 거품과 진한 맛", "image": "cappuccino"}
            ]
            

        """.trimIndent()
        }
    }



    val _recommendedMenus = MutableStateFlow<List<RecommendedMenu>>(emptyList())
    val recommendedMenus: StateFlow<List<RecommendedMenu>> = _recommendedMenus

    fun parseRecommendedMenu(jsonString: String): List<RecommendedMenu> {
        val cleaned = try {
            // 문자열이 따옴표로 감싸진 JSON 배열일 경우 처리
            if (jsonString.trim().startsWith("\"[")) {
                jsonString
                    .replace("\\n", "")
                    .replace("\\", "")
                    .removeSurrounding("\"")
            } else {
                jsonString
            }
        } catch (e: Exception) {
            Log.e("GPT", "JSON 정리 실패: ${e.message}")
            return emptyList()
        }

        return try {
            val listType = object : TypeToken<List<RecommendedMenu>>() {}.type
            Gson().fromJson(cleaned, listType)
        } catch (e: Exception) {
            Log.e("GPT", "Gson 파싱 실패: ${e.message}")
            emptyList()
        }
    }




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

    fun fetchRecommendedMenus(apiKey: String, isKorean: Boolean) {
        viewModelScope.launch {
            try {
                val prompt = getLocalizedPrompt(isKorean)
                val request = ChatRequest(
                    model = "gpt-4o",
                    messages = listOf(Message("user", prompt))
                )
                val response = RetrofitClient.create(apiKey).getChatCompletion(request)
                val content = response.choices.firstOrNull()?.message?.content ?: "[]"
                _recommendedMenus.value = parseRecommendedMenu(content)

            } catch (e: Exception) {
                Log.e("GPT", "추천 메뉴 오류: ${e.message}")
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
