package com.ssafy.nhcafe.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.dto.UserInfo
import com.ssafy.nhcafe.ui.common.TopBar
import com.ssafy.nhcafe.viewModel.GPTViewModel

@Composable
fun StampScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit,
    phoneNumber: String, // 전달받은 전화번호
    gptViewModel: GPTViewModel
) {

    val context = LocalContext.current
    var stampCount by remember { mutableStateOf<Int?>(null) }

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

        Text(
            text = if (isKorean) "스탬프 적립 현황" else "Your Stamp Status",
            fontSize = 16.sp,
            color = Color(0xFF5D2C15)
        )

        Text(
            text = if (stampCount != -1) "${stampCount ?: 0}" else if (isKorean) "가입 축하 드려요!!" else "Welcome Aboard!",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDD6E1F)
        )

        Text(
            text = if (stampCount != -1) (if (isKorean) "개 보유" else "Stamps") else "",
            fontSize = 16.sp,
            color = Color(0xFF5D2C15)
        )

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
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (stampCount != -1) {
                        if (isKorean) "${stampCount ?: 0}개를 사용하시겠어요?" else "Use ${stampCount ?: 0} stamp(s)?"
                    } else {
                        if (isKorean) "다음 번에 스탬프를 사용해보세요!" else "Try using stamps next time!"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDD6E1F)
                )

                Text(
                    text = if (isKorean) "스탬프 1개당 100원 할인!" else "₩100 discount per stamp!",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 스탬프 사용하기 버튼
        Button(
            onClick = {
                stampCount?.let { count ->
                    gptViewModel.sendOrder(phoneNumber, count, onSuccess = { orderId ->
                        navController.navigate("completeOrder/$orderId") {
                            popUpTo("main") { inclusive = false }
                        }
                    }, onFailure = {
                        Toast.makeText(context, "주문 실패", Toast.LENGTH_SHORT).show()
                    })
                }

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
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isKorean) "스탬프 사용하기" else "Use Stamps",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 건너뛰기 버튼
        Button(
            onClick = {
                stampCount?.let { count ->
                    gptViewModel.sendOrder(phoneNumber, 0, onSuccess = { orderId ->
                        navController.navigate("completeOrder/$orderId") {
                            popUpTo("main") { inclusive = false }
                        }
                    }, onFailure = {
                        Toast.makeText(context, "주문 실패", Toast.LENGTH_SHORT).show()
                    })
                }
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
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isKorean) "이번엔 사용하지 않음" else "Skip This Time",
                color = Color(0xFF5D2C15),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "© 2025 ${if (isKorean) "무인 AI카페" else "Unmanned AI Cafe"}",
            fontSize = 12.sp,
            color = Color.LightGray
        )


    }

    LaunchedEffect(Unit) {
        gptViewModel.getOrCreateUser(phoneNumber) {
            stampCount = it
        }
    }


}
