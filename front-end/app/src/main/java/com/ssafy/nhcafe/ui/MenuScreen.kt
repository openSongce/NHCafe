package com.ssafy.nhcafe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.ui.common.TopBar

@Composable
fun MenuScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit
) {
    val categories = listOf("커피", "논커피", "디저트", "에이드", "티")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    val scrollState = rememberScrollState()

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

            MenuGrid(category = selectedCategory)
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
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

    }

}



@Composable
fun MenuGrid(category: String) {
    val menuItems = when (category) {
        "커피" -> listOf("카페라떼", "아메리카노", "카푸치노", "에스프레소")
        "에이드" -> listOf("레몬에이드", "자몽에이드")
        "티" -> listOf("허브티", "얼그레이티")
        else -> listOf("기본 메뉴")
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(menuItems) { item ->
            MenuItemCard(name = item)
        }
    }
}

@Composable
fun MenuItemCard(name: String) {
    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(180.dp), // 여유 있게 조정
        contentAlignment = Alignment.Center // 전체 중앙 정렬
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.temp_latte),
                contentDescription = "latte",
                modifier = Modifier
                    .size(70.dp).clip(CircleShape)
                    .background(Color.LightGray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(name, fontWeight = FontWeight.SemiBold)
            Text("₩3,800", color = Color(0xFFDD6E1F))
        }
    }
}

