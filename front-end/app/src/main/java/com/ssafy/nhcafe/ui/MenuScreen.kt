package com.ssafy.nhcafe.ui

import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.nhcafe.BuildConfig
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.api.CafeApiClient
import com.ssafy.nhcafe.dto.MenuItem
import com.ssafy.nhcafe.ui.common.TopBar
import com.ssafy.nhcafe.viewModel.GPTViewModel


val apiKey = BuildConfig.OPEN_API_KEY

val nameEnMap = mapOf(
    "아메리카노" to "Americano",
    "빈티지 라떼" to "Vintage Latte",
    "시나몬 콜드브루" to "Cinnamon Cold Brew",
    "바닐라 크림 커피" to "Vanilla Cream Coffee",
    "더치 초코 모카" to "Dutch Choco Mocha",
    "아인슈페너" to "Einspanner",
    "카라멜 너츠 라떼" to "Caramel Nuts Latte",

    "말차 크림 라떼" to "Matcha Cream Latte",
    "흑임자 라떼" to "Black Sesame Latte",
    "딸기 밀크" to "Strawberry Milk",
    "로얄 밀크티" to "Royal Milk Tea",
    "초코 크림 라떼" to "Choco Cream Latte",
    "블루베리 요거트" to "Blueberry Yogurt",

    "자몽 에이드" to "Grapefruit Ade",
    "레몬 민트 에이드" to "Lemon Mint Ade",
    "라임 오렌지 에이드" to "Lime Orange Ade",
    "청포도 에이드" to "Green Grape Ade",
    "블루레몬 에이드" to "Blue Lemon Ade",
    "복숭아 에이드" to "Peach Ade",

    "딸기 생크림 케이크" to "Strawberry Fresh Cream Cake",
    "티라미수" to "Tiramisu",
    "바스크 치즈케이크" to "Basque Cheesecake",
    "말차 마들렌" to "Matcha Madeleine",
    "카라멜 브라우니" to "Caramel Brownie",
    "무화과 타르트" to "Fig Tart",

    "캐모마일 블렌드" to "Chamomile Blend",
    "얼그레이 티" to "Earl Grey Tea",
    "루이보스 바닐라" to "Rooibos Vanilla",
    "자몽 허니 블랙티" to "Grapefruit Honey Black Tea",
    "페퍼민트 티" to "Peppermint Tea",
    "유자차" to "Citron Tea"
)

val descEnMap = mapOf(
    "에스프레소에 물을 더해 깔끔하고 쌉싸름한 풍미가 매력적인 기본 커피" to
            "A basic coffee with a clean and bitter taste by adding water to espresso",
    "고소한 우유에 깊은 에스프레소를 더한 감성적인 라떼" to
            "A sentimental latte with savory milk and rich espresso",
    "은은한 시나몬 향이 감도는 시원한 콜드브루" to
            "A refreshing cold brew with a subtle cinnamon aroma",
    "바닐라의 달콤함과 크림의 부드러움이 어우러진 커피" to
            "A delightful blend of vanilla sweetness and soft cream",
    "더치커피에 진한 초콜릿이 섞인 부드러운 모카" to
            "A smooth mocha combining Dutch coffee and rich chocolate",
    "깊고 진한 커피 위에 달콤한 크림이 올라간 비엔나풍 커피" to
            "A Viennese-style coffee with sweet cream on deep and bold coffee",
    "고소한 너트와 달콤한 카라멜의 조화, 감성 가득 라떼" to
            "A heartwarming latte blending caramel and nutty flavors",

    "진한 말차와 크림이 어우러진 부드러운 라떼" to
            "A smooth latte with rich matcha and soft cream",
    "고소한 흑임자와 우유가 어우러진 한국적 감성 라떼" to
            "A Korean-inspired latte with savory black sesame and milk",
    "직접 만든 딸기청과 신선한 우유가 어우러진 달콤한 음료" to
            "Sweet drink made with homemade strawberry syrup and fresh milk",
    "진한 홍차와 우유의 조화, 부드럽고 은은한 풍미" to
            "A soft and mellow harmony of strong black tea and milk",
    "진한 초코와 우유, 크림이 섞인 부드러운 음료" to
            "A rich and smooth drink mixed with chocolate, milk, and cream",
    "블루베리와 요거트가 어우러져 상큼한 맛을 선사하는 음료" to
            "A refreshing drink made with blueberries and yogurt",

    "상큼한 자몽과 탄산이 어우러진 청량한 에이드" to
            "A refreshing ade with tangy grapefruit and sparkling soda",
    "상큼한 레몬과 시원한 민트가 어우러진 시그니처 에이드" to
            "A signature ade with zesty lemon and cool mint",
    "톡 쏘는 라임과 달콤한 오렌지가 조화를 이루는 에이드" to
            "An ade combining tangy lime and sweet orange",
    "청포도의 싱그러움이 느껴지는 상큼한 음료" to
            "A refreshing drink with the freshness of green grapes",
    "블루 큐라소 시럽으로 시각도 사로잡는 시원한 에이드" to
            "A cooling ade visually appealing with blue curacao syrup",
    "달콤한 복숭아 과육이 가득한 에이드" to
            "An ade filled with sweet peach chunks",

    "신선한 딸기와 부드러운 생크림이 조화를 이룬 케이크" to
            "A cake with fresh strawberries and soft whipped cream",
    "진한 에스프레소와 크림이 어우러진 이탈리안 디저트" to
            "An Italian dessert with rich espresso and creamy layers",
    "겉은 바삭, 속은 촉촉한 스페인식 치즈케이크" to
            "A Spanish-style cheesecake with a crispy outside and moist inside",
    "쌉싸름한 말차의 향이 은은하게 퍼지는 부드러운 마들렌" to
            "A soft madeleine with a gently spreading matcha aroma",
    "진한 초콜릿과 고소한 카라멜이 어우러진 브라우니" to
            "A rich brownie combining chocolate and nutty caramel",
    "무화과의 달콤함과 고소한 타르트가 어우러진 고급 디저트" to
            "A luxurious dessert blending sweet figs and buttery tart crust",

    "은은한 캐모마일 향이 마음을 편안하게 해주는 허브티" to
            "A calming herbal tea with a delicate chamomile aroma",
    "베르가못 향이 감도는 클래식한 홍차" to
            "A classic black tea with the scent of bergamot",
    "무카페인 루이보스에 달콤한 바닐라 향을 더한 차" to
            "A caffeine-free tea with sweet vanilla aroma",
    "상큼한 자몽과 달콤한 꿀이 어우러진 따뜻한 블랙티" to
            "A warm black tea with grapefruit zest and sweet honey",
    "입안을 상쾌하게 해주는 청량한 허브티" to
            "A cool and refreshing herbal tea for a clean palate",
    "향긋한 유자와 따뜻한 온기가 감성을 자극하는 전통차" to
            "A traditional Korean tea with fragrant citron and soothing warmth"
)


val categoryMap = mapOf(
    "Coffee" to "커피",
    "Non-Coffee" to "논커피",
    "Dessert" to "디저트",
    "Ade" to "에이드",
    "Tea" to "티"
)


@Composable
fun MenuScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit,
    gptViewModel: GPTViewModel
) {
    val categories = if (isKorean) categoryMap.values.toList() else categoryMap.keys.toList()
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    val scrollState = rememberScrollState()
    var allMenus by remember { mutableStateOf(listOf<MenuItem>()) }

    // MenuScreen 내부 일부만 수정
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
    ) {
        TopBar(isKorean, onLanguageToggle)

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier
            .weight(1f) // 나머지 영역을 MenuGrid에 할당
            .padding(horizontal = 20.dp)) {

            // 카테고리 선택
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category) Color(0xFFD28B36) else Color.White
                        )
                    ) {
                        Text(
                            text = category,
                            color = if (selectedCategory == category) Color.White else Color(0xFF5D2C15)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = selectedCategory,
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF5D2C15)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val selectedCategoryEnglish = if (isKorean) {
                categoryMap.entries.firstOrNull { it.value == selectedCategory }?.key ?: selectedCategory
            } else {
                selectedCategory
            }

            MenuGrid(
                category = selectedCategoryEnglish,
                menus = allMenus,
                isKorean = isKorean,
                gptViewModel = gptViewModel
            )
        }

        // 수정된 하단 마이크 버튼 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { navController.navigate("conversation") },
                shape = CircleShape,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDFA878),
                    contentColor = Color.White
                ),
                modifier = Modifier.size(80.dp) // 버튼만 size 지정
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mic),
                        contentDescription = "Mic Icon",
                        modifier = Modifier.size(30.dp) // 아이콘 크기 조절
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isKorean) "말하기" else "Speak",
                        fontSize = if (isKorean) 12.sp else 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

    }

    LaunchedEffect(Unit) {
        try {
            val response = CafeApiClient.apiService.getMenuList()
            if (response.isSuccessful) {
                allMenus = response.body() ?: emptyList()
                // 상태 저장해서 LazyColumn 등에 표시
                Log.d("MenuScreen", "불러온 메뉴: $allMenus")
            } else {
                Log.e("MenuScreen", "응답 실패: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MenuScreen", "에러: ${e.message}")
        }
    }

}



@Composable
fun MenuGrid(
    category: String,
    menus: List<MenuItem>,
    isKorean: Boolean,
    gptViewModel: GPTViewModel
) {


    val filteredMenus = menus.filter {
        it.type.equals(category, ignoreCase = true)
    }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(filteredMenus) { item ->
            MenuItemCard(item = item, isKorean = isKorean, onItemClick = { selectedItem = it })
        }
    }

    // 다이얼로그 표시
    selectedItem?.let { item ->
        AlertDialog(
            onDismissRequest = {
                selectedItem = null
                gptViewModel.stopTTS()
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedItem = null
                    gptViewModel.stopTTS()
                }) {
                    Text(
                        text = if (isKorean) "닫기" else "Close",
                        color = Color(0xFF5D2C15)
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color(0xFFFDF6F0),
            title = {
                Text(
                    text = if (isKorean) item.name else nameEnMap[item.name] ?: item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF3E2723)
                )
            },
            text = {
                val context = LocalContext.current
                val resourceName = camelToSnakeCase(item.img)
                val imageResId = getDrawableIdByName(context, resourceName)
                val validImageResId = if (imageResId != 0) imageResId else R.drawable.temp_latte

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = validImageResId),
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF3E0))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isKorean) item.pDesc else descEnMap[item.pDesc] ?: item.pDesc,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${item.price}원",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE7662A)
                    )
                }
            }
        )

        // TTS 실행
        LaunchedEffect(item) {
            gptViewModel.stopTTS()
            gptViewModel.playTTS(
                if (isKorean)
                    "${item.name} ${item.pDesc} ${item.price}원"
                else
                    "${nameEnMap[item.name] ?: item.name} ${descEnMap[item.pDesc] ?: item.pDesc} ${item.price} won",
                apiKey
            )
        }
    }
}
fun camelToSnakeCase(name: String): String {
    return name.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
}


@DrawableRes
fun getDrawableIdByName(context: Context, name: String): Int {
    return context.resources.getIdentifier(name, "drawable", context.packageName)
}

@Composable
fun MenuItemCard(item: MenuItem,isKorean: Boolean, onItemClick: (MenuItem) -> Unit) {

    val context = LocalContext.current
    val resourceName = camelToSnakeCase(item.img)
    val imageResId = getDrawableIdByName(context, resourceName)
    val validImageResId = if (imageResId != 0) imageResId else R.drawable.temp_latte

    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onItemClick(item) },  // ← 클릭 시 콜백
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = validImageResId),
                contentDescription = item.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isKorean) item.name else nameEnMap[item.name] ?: item.name,
                fontWeight = FontWeight.SemiBold
            )
            Text(item.price + "원", color = Color(0xFFDD6E1F))
        }
    }
}
