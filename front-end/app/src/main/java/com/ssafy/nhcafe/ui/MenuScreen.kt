package com.ssafy.nhcafe.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.dto.MenuItem
import com.ssafy.nhcafe.ui.common.TopBar
import com.ssafy.nhcafe.viewModel.GPTViewModel

val allMenus = listOf(
    MenuItem("카페라떼", "부드러운 우유의 풍미", "₩3,800", R.drawable.temp_latte, "커피"),
    MenuItem("아메리카노", "진한 커피향", "₩3,500", R.drawable.temp_americano, "커피"),
    MenuItem("카푸치노", "풍부한 거품", "₩4,000", R.drawable.temp_cappuccino, "커피"),
    MenuItem("레몬에이드", "상큼한 레몬맛", "₩4,200", R.drawable.temp_latte, "에이드"),
    MenuItem("자몽에이드", "쌉싸름한 자몽", "₩4,300", R.drawable.temp_latte, "에이드"),
    MenuItem("허브티", "편안한 허브향", "₩3,800", R.drawable.temp_latte, "티"),
    MenuItem("치즈케이크", "진한 치즈맛", "₩4,500", R.drawable.temp_latte, "디저트"),
    MenuItem("초코우유", "달콤한 초코", "₩3,000", R.drawable.temp_latte, "논커피")
)
val apiKey = "sREDACTED_PROJECT_KEY"


@Composable
fun MenuScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit
) {
    val categories = listOf("커피", "논커피", "디저트", "에이드", "티")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    val scrollState = rememberScrollState()
    val gptViewModel: GPTViewModel = viewModel()

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

            MenuGrid(category = selectedCategory, gptViewModel = gptViewModel)
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

}



@Composable
fun MenuGrid(category: String, gptViewModel: GPTViewModel) {
    val filteredMenus = allMenus.filter { it.category == category }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(filteredMenus) { item ->
            MenuItemCard(item = item, onItemClick = { selectedItem = it })
        }
    }

    // 다이얼로그 표시
    selectedItem?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedItem = null
                gptViewModel.stopTTS()},
            confirmButton = {
                TextButton(onClick = { selectedItem = null
                    gptViewModel.stopTTS()}) {
                    Text("닫기", color = Color(0xFF5D2C15))
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color(0xFFFDF6F0),  // 부드러운 베이지 계열
            title = {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF3E2723)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF3E0))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = item.description,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE7662A)
                    )
                }
            }
        )


        // TTS 실행
        LaunchedEffect(item) {
            gptViewModel.stopTTS()  // 이전 TTS 중지
            gptViewModel.playTTS(item.name + " " + item.description + " " + item.price + "원", apiKey)
        }
    }
}



@Composable
fun MenuItemCard(item: MenuItem, onItemClick: (MenuItem) -> Unit) {
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
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.name, fontWeight = FontWeight.SemiBold)
            Text(item.price, color = Color(0xFFDD6E1F))
        }
    }
}
