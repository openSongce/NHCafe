package com.ssafy.nhcafe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.nhcafe.BuildConfig
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.api.RecommendedMenu
import com.ssafy.nhcafe.ui.common.TopBar
import com.ssafy.nhcafe.viewModel.GPTViewModel




@Composable
fun MainScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit,
    gptViewModel: GPTViewModel
) {

    val recommendedMenus by gptViewModel.recommendedMenus.collectAsState()
    val apiKey = BuildConfig.OPEN_API_KEY


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
            .padding(horizontal = 20.dp)
    ) {
        TopBar(isKorean, onLanguageToggle)
        Spacer(modifier = Modifier.height(12.dp))
        GreetingCard(isKorean)
        Spacer(modifier = Modifier.height(20.dp))
        RecommendedMenuSection(isKorean = isKorean, menus = recommendedMenus)
        Spacer(modifier = Modifier.weight(1f))
        MenuSection(isKorean, moreClick = { navController.navigate("menu")})
        Spacer(modifier = Modifier.weight(1f))
        VoiceInputSection(
            isKorean = isKorean,
            onSpeakClick = { navController.navigate("conversation") }
        )
    }


    LaunchedEffect(isKorean) {
        gptViewModel.fetchRecommendedMenus(apiKey, isKorean)
    }

}

@Composable
fun GreetingCard(isKorean: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color(0x33000000), // 부드러운 회색 그림자
                spotColor = Color(0x55000000)     // 그림자 강조 색
            )
            .background(Color(0xFFFCEBDD), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    )
    {
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
fun RecommendedMenuSection(isKorean: Boolean, menus: List<RecommendedMenu>) {
    Column {
        Text(
            text = if (isKorean) "추천 메뉴" else "Recommended Menu",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF5D2C15)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            menus.forEach { menu ->
                val imageRes = when (menu.image) {
                    "latte" -> R.drawable.temp_latte
                    "americano" -> R.drawable.temp_americano
                    "cappuccino" -> R.drawable.temp_cappuccino
                    else -> R.drawable.temp_latte
                }
                MenuCard(imageRes, menu.name, menu.description)
            }
        }
    }
}



@Composable
fun MenuSection(isKorean: Boolean, moreClick : () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isKorean) "매장 메뉴" else "Menu",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF5D2C15)
            )
            Text(
                text = if (isKorean) "더보기" else "More",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    moreClick()
                }
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
            .shadow(6.dp, shape = RoundedCornerShape(12.dp))
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.size(48.dp).shadow(4.dp, shape = CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis // 긴 제목 말줄임표
        )

        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 2, // 설명은 2줄까지
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Composable
fun VoiceInputSection(isKorean: Boolean,  onSpeakClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onSpeakClick,
            shape = CircleShape,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDFA878),
                contentColor = Color.White
            ),
            modifier = Modifier.size(100.dp)
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



