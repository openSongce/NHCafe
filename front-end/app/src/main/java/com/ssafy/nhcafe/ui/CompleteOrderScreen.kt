package com.ssafy.nhcafe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ssafy.nhcafe.ui.common.TopBar
import com.ssafy.nhcafe.viewModel.GPTViewModel

@Composable
fun CompleteOrderScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit,
    orderNumber: Int = 123 // 기본값 예시
) {
    val gptViewModel: GPTViewModel = viewModel()
    val apiKey = "sREDACTED_PROJECT_KEY"

    LaunchedEffect(Unit) {
        val message = if (isKorean) "주문이 완료되었습니다. 주문번호는 $orderNumber 번입니다." else "Order completed. Your order number is $orderNumber."
        gptViewModel.playTTS(message, apiKey)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(isKorean = isKorean, onLanguageToggle = onLanguageToggle)

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.coffee_icon),
            contentDescription = "커피 아이콘",
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, CircleShape)
                .padding(20.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isKorean) "주문이 완료되었습니다!" else "Order Completed!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D2C15)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isKorean) "주문번호 $orderNumber" else "Order No. $orderNumber",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE7662A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.voice_icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isKorean) "음성 안내가 곧 재생됩니다." else "Voice guide will play soon.",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navController.navigate("conversation"){
                popUpTo("main"){inclusive=false}
            }  },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7C3C))
        ) {
            Image(
                painter = painterResource(id = R.drawable.plus_icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("새로 주문하기", fontWeight = FontWeight.SemiBold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("main"){
                popUpTo("main"){inclusive=true}
            } },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDE4D1))
        ) {
            Text("홈 화면으로", color = Color(0xFF5D2C15))
        }

        Spacer(modifier = Modifier.weight(1f))

        Text("© 2025 무인 AI카페", fontSize = 12.sp, color = Color.LightGray)
    }
}
