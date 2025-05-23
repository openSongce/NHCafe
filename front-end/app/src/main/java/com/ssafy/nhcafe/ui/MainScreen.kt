package com.ssafy.nhcafe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.nhcafe.R

@Composable
fun MainScreen() {
    var isKorean by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
            .padding(horizontal = 20.dp)
    ) {
        TopBarSection(isKorean) { isKorean = !isKorean }
        Spacer(modifier = Modifier.height(12.dp))
        GreetingCard(isKorean)
        Spacer(modifier = Modifier.height(20.dp))
        RecommendedMenuSection(isKorean)
        Spacer(modifier = Modifier.weight(1f))
        VoiceInputSection(isKorean)
    }
}

@Composable
fun TopBarSection(isKorean: Boolean, onLanguageToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo_splash),
                contentDescription = "NHCafe Logo",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isKorean) "NHCafe - 인동점" else "NHCafe - Indong Branch",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF5D2C15)
            )
        }


        TextButton(onClick = onLanguageToggle) {
            Text(
                text = if (isKorean) "🇰🇷" else "🇺🇸",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun GreetingCard(isKorean: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFCEBDD), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = if (isKorean)
                "오늘 하루도 따뜻하게, 좋은 하루 보내세요 :)"
            else
                "Have a warm and lovely day :)",
            fontSize = 16.sp,
            color = Color(0xFF5D2C15)
        )
    }
}

@Composable
fun RecommendedMenuSection(isKorean: Boolean) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isKorean) "추천 메뉴" else "Recommended Menu",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF5D2C15)
            )
            Text(
                text = if (isKorean) "더보기" else "More",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MenuCard(R.drawable.temp_latte, if (isKorean) "카페라떼" else "Café Latte", if (isKorean) "부드러운 라떼" else "Smooth Latte")
            MenuCard(R.drawable.temp_americano, if (isKorean) "아메리카노" else "Americano", if (isKorean) "진한 커피향" else "Deep Coffee")
            MenuCard(R.drawable.temp_cappuccino, if (isKorean) "카푸치노" else "Cappuccino", if (isKorean) "풍부한 거품" else "Rich Foam")
        }
    }
}

@Composable
fun MenuCard(imageRes: Int, title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(subtitle, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun VoiceInputSection(isKorean: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { /* TODO: 음성 인식 로직 */ },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDFA878),
                contentColor = Color.White
            ),
            modifier = Modifier.size(90.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "Mic Icon",
                    modifier = Modifier.size(40.dp) // 아이콘 크기 조정
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isKorean) "말하기" else "Speak",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isKorean) "버튼을 누르고 음성으로 주문해보세요" else "Tap the button to order by voice",
            fontSize = 13.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "© 2025 무인카페 AI Barista",
            fontSize = 10.sp,
            color = Color.LightGray
        )
    }
}


