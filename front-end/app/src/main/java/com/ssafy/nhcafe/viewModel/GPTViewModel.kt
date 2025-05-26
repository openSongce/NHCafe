package com.ssafy.nhcafe.viewModel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.nhcafe.api.*
import com.ssafy.nhcafe.dto.CartItem
import com.ssafy.nhcafe.dto.MenuItem
import com.ssafy.nhcafe.dto.OrderDetail
import com.ssafy.nhcafe.dto.OrderRequest
import com.ssafy.nhcafe.dto.UserInfo
import com.ssafy.nhcafe.dto.UserRequest
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
            content = "너는 NHCafe 직원이야. 사용자가 메뉴를 말하면 장바구니에 몇잔 담았다고 안내해줘." +
                    " 다른 말에 대해서는 카페직원이라고 생각하고 말을 해줘." +
                    "우리 매장 메뉴는 우리 매장 메뉴는 아메리카노(Coffee, 4500원): 에스프레소에 물을 더해 깔끔하고 쌉싸름한 풍미가 매력적인 기본 커피, 빈티지 라떼(Coffee, 5000원): 고소한 우유에 깊은 에스프레소를 더한 감성적인 라떼, 시나몬 콜드브루(Coffee, 5500원): 은은한 시나몬 향이 감도는 시원한 콜드브루, 바닐라 크림 커피(Coffee, 5800원): 바닐라의 달콤함과 크림의 부드러움이 어우러진 커피, 더치 초코 모카(Coffee, 6000원): 더치커피에 진한 초콜릿이 섞인 부드러운 모카, 아인슈페너(Coffee, 6200원): 깊고 진한 커피 위에 달콤한 크림이 올라간 비엔나풍 커피, 카라멜 너츠 라떼(Coffee, 5800원): 고소한 너트와 달콤한 카라멜의 조화, 감성 가득 라떼,\n" +
                    "\n" +
                    "말차 크림 라떼(Non-Coffee, 6000원): 진한 말차와 크림이 어우러진 부드러운 라떼, 흑임자 라떼(Non-Coffee, 5800원): 고소한 흑임자와 우유가 어우러진 한국적 감성 라떼, 딸기 밀크(Non-Coffee, 5500원): 직접 만든 딸기청과 신선한 우유가 어우러진 달콤한 음료, 로얄 밀크티(Non-Coffee, 5800원): 진한 홍차와 우유의 조화, 부드럽고 은은한 풍미, 초코 크림 라떼(Non-Coffee, 5500원): 진한 초코와 우유, 크림이 섞인 부드러운 음료, 블루베리 요거트(Non-Coffee, 5800원): 블루베리와 요거트가 어우러져 상큼한 맛을 선사하는 음료,\n" +
                    "\n" +
                    "자몽 에이드(Ade, 5500원): 상큼한 자몽과 탄산이 어우러진 청량한 에이드, 레몬 민트 에이드(Ade, 5500원): 상큼한 레몬과 시원한 민트가 어우러진 시그니처 에이드, 라임 오렌지 에이드(Ade, 5800원): 톡 쏘는 라임과 달콤한 오렌지가 조화를 이루는 에이드, 청포도 에이드(Ade, 5300원): 청포도의 싱그러움이 느껴지는 상큼한 음료, 블루레몬 에이드(Ade, 5800원): 블루 큐라소 시럽으로 시각도 사로잡는 시원한 에이드, 복숭아 에이드(Ade, 5500원): 달콤한 복숭아 과육이 가득한 에이드,\n" +
                    "\n" +
                    "딸기 생크림 케이크(Dessert, 6000원): 신선한 딸기와 부드러운 생크림이 조화를 이룬 케이크, 티라미수(Dessert, 5500원): 진한 에스프레소와 크림이 어우러진 이탈리안 디저트, 바스크 치즈케이크(Dessert, 5800원): 겉은 바삭, 속은 촉촉한 스페인식 치즈케이크, 말차 마들렌(Dessert, 4000원): 쌉싸름한 말차의 향이 은은하게 퍼지는 부드러운 마들렌, 카라멜 브라우니(Dessert, 4500원): 진한 초콜릿과 고소한 카라멜이 어우러진 브라우니, 무화과 타르트(Dessert, 6500원): 무화과의 달콤함과 고소한 타르트가 어우러진 고급 디저트,\n" +
                    "\n" +
                    "캐모마일 블렌드(Tea, 5000원): 은은한 캐모마일 향이 마음을 편안하게 해주는 허브티, 얼그레이 티(Tea, 5000원): 베르가못 향이 감도는 클래식한 홍차, 루이보스 바닐라(Tea, 5200원): 무카페인 루이보스에 달콤한 바닐라 향을 더한 차, 자몽 허니 블랙티(Tea, 5500원): 상큼한 자몽과 달콤한 꿀이 어우러진 따뜻한 블랙티, 페퍼민트 티(Tea, 5000원): 입안을 상쾌하게 해주는 청량한 허브티, 유자차(Tea, 5300원): 향긋한 유자와 따뜻한 온기가 감성을 자극하는 전통차 입니다.\n"
        )
    )
    val chatResponse: StateFlow<String> get() = _chatResponse
    private val _menuList = mutableStateListOf<MenuItem>()
    val menuList: List<MenuItem> get() = _menuList

    init {
        fetchMenus()
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            try {
                val response = CafeApiClient.apiService.getMenuList()
                if (response.isSuccessful) {
                    _menuList.clear()
                    _menuList.addAll(response.body() ?: emptyList())
                } else {
                    Log.e("GPTViewModel", "메뉴 불러오기 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("GPTViewModel", "메뉴 요청 에러: ${e.message}")
            }
        }
    }


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

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    fun handleUserInput(text: String) {
        val names = extractMenuNames(text)
        val quantity = extractQuantity(text)  // 문장에 수량이 하나만 있을 경우 공통 수량

        if (names.isEmpty()) {
            return
        }

        val updated = _cartItems.value.toMutableList()

        for (name in names) {
            val matchedMenu = _menuList.find { it.name == name }
            matchedMenu?.let { menu ->
                val existing = updated.find { it.name == name }
                if (existing != null) {
                    val newItem = existing.copy(count = existing.count + quantity)
                    updated[updated.indexOf(existing)] = newItem
                } else {
                    updated.add(CartItem(name = menu.name, price = menu.price, count = quantity))
                }
            }
        }

        _cartItems.value = updated

    }


    private fun extractQuantity(text: String): Int {
        // 한글 숫자 → 정수 매핑
        val koreanNumberMap = mapOf(
            "한" to 1, "두" to 2, "세" to 3, "네" to 4, "다섯" to 5,
            "여섯" to 6, "일곱" to 7, "여덟" to 8, "아홉" to 9, "열" to 10
        )

        // 1. 아라비아 숫자 먼저 체크 (예: "3잔", "5개")
        val numberRegex = Regex("""(\d+)\s*(잔|개|명|번)?""")
        val numberMatch = numberRegex.find(text)
        if (numberMatch != null) {
            return numberMatch.groups[1]?.value?.toIntOrNull() ?: 1
        }

        // 2. 한글 숫자 체크 (예: "두 잔", "한 개")
        for ((koreanWord, numberValue) in koreanNumberMap) {
            if (text.contains("$koreanWord ")) return numberValue
            if (text.contains("${koreanWord}잔") || text.contains("${koreanWord}개")) return numberValue
        }

        // 3. 기본 수량
        return 1
    }


    private fun extractMenuNames(text: String): List<String> {
        val normalizedText = text.replace(" ", "")  // 입력에서 공백 제거

        return _menuList
            .map { it.name to it.name.replace(" ", "") } // 메뉴 이름도 공백 제거
            .filter { (_, normalizedName) -> normalizedText.contains(normalizedName) }
            .map { (originalName, _) -> originalName }
    }



    fun sendOrder(phoneNumber: String?, usedStamp: Int, onSuccess: (Int) -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val cart = cartItems.value
                val productMap = menuList.associateBy { it.name }

                val details = cart.mapNotNull {
                    val product = productMap[it.name]
                    product?.let { p -> OrderDetail(productId = p.id, quantity = it.count) }
                }

                val totalPrice = cart.sumOf { it.count * it.price.toInt() } - (usedStamp * 100)

                val orderRequest = OrderRequest(
                    userId = phoneNumber ?: "guest",
                    usedStamp = usedStamp,
                    price = totalPrice,
                    details = details
                )

                val response = CafeApiClient.apiService.placeOrder(orderRequest)
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: -1)
                } else {
                    onFailure()
                }
            } catch (e: Exception) {
                Log.e("Order", "에러: ${e.message}")
                onFailure()
            }
        }
    }



    fun getOrCreateUser(phoneNumber: String, onResult: (Int?) -> Unit) {
        viewModelScope.launch {
            try {
                val request = UserRequest(number = phoneNumber)
                val response = CafeApiClient.apiService.getOrCreateUser(request)

                if (response.isSuccessful) {
                    val stampCount = response.body()
                    onResult(stampCount)
                } else {
                    Log.e("UserAPI", "응답 실패: ${response.code()} ${response.message()}")
                    onResult(null)
                }
            } catch (e: Exception) {
                Log.e("UserAPI", "예외 발생: ${e.message}")
                onResult(null)
            }
        }
    }


    fun clearCart() {
        _cartItems.value = emptyList()
    }


}
