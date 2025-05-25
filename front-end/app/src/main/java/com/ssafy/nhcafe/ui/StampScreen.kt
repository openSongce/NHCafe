package com.ssafy.nhcafe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun StampScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit,
    phoneNumber: String // 전달받은 전화번호
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(isKorean = isKorean, onLanguageToggle = onLanguageToggle)

        Spacer(modifier = Modifier.height(32.dp))

        Icon(
            painter = painterResource(id = R.drawable.stamp),
            contentDescription = "스탬프",
            tint = Color(0xFFD28B36),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("스탬프 적립 현황", fontSize = 16.sp, color = Color(0xFF5D2C15))
        Text("7", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDD6E1F))
        Text("개 보유", fontSize = 16.sp, color = Color(0xFF5D2C15))

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF7E9), shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.gift1),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("스탬프 7개를 사용하시겠어요?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("스탬프 1개당 100원 할인!", color = Color.Gray, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 스탬프 사용하기 버튼
        Button(
            onClick = {
                // TODO: 할인 적용 후 다음 화면으로 이동
                navController.navigate("completeOrder/{orderNumber}"){
                    popUpTo("main"){inclusive=false}
                }  // 예시
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7C3C)),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.check),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("스탬프 사용하기", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 건너뛰기 버튼
        Button(
            onClick = {
                navController.navigate("completeOrder/{orderNumber}"){
                    popUpTo("main"){inclusive=false}
                }  // 스킵 시에도 다음 화면
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDE4D1)),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.x),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("이번엔 사용하지 않음", color = Color(0xFF5D2C15), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text("© 2025 무인 AI카페", fontSize = 12.sp, color = Color.LightGray)
    }
}
