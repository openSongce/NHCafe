package com.ssafy.nhcafe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.ui.common.TopBar

@Composable
fun OrderConfirmScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
            .padding(horizontal = 20.dp)
    ) {
        TopBar(
            isKorean = isKorean,
            onLanguageToggle = onLanguageToggle,

        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)

        ) {
            Column {
                Text("주문 내역", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5D2C15))
                Spacer(modifier = Modifier.height(12.dp))

                OrderItem("아메리카노", "Hot", 2, 8000)
                OrderItem("카페라떼", "Ice", 1, 4500)
                OrderItem("바닐라라떼", "Hot", 1, 5000)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFCEBDD), shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("총 수량", color = Color(0xFF5D2C15))
                Spacer(modifier = Modifier.height(4.dp))
                Text("총 결제금액", color = Color(0xFF5D2C15))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("4잔", color = Color(0xFF5D2C15))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "17,500원",
                    color = Color(0xFFE7662A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "☕ 주문 내역을 확인해 주세요.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF5D2C15),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEF5ED))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 취소 버튼 (비율 고정)
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCEBDD)),
                elevation = ButtonDefaults.buttonElevation(6.dp),
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "취소",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            }

            // 🔸 주문 버튼 (강조 색상, 아이콘 포함)
            Button(
                onClick = { navController.navigate("phoneNumberInput") },
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5D2C15),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(56.dp)
                    .weight(2f) // 더 길게
            ) {
                Image(
                    painter = painterResource(id = R.drawable.buy),
                    contentDescription = "장바구니",
                    modifier = Modifier
                        .size(30.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "주문하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }


        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun OrderItem(name: String, temp: String, count: Int, price: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (temp == "Hot") Color(0xFFD28B36) else Color(0xFFBD4B4B)
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("$name", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(temp, fontSize = 12.sp, color = Color.Gray)
        }

        Text("$count 잔  ${"%,d".format(price)}원", fontSize = 14.sp)
    }
}

