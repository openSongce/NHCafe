package com.ssafy.nhcafe.ui

import android.app.Application
import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.ssafy.nhcafe.viewModel.GPTViewModel

@Composable
fun OrderConfirmScreen(
    navController: NavController,
    isKorean: Boolean,
    onLanguageToggle: () -> Unit,
    gptViewModel: GPTViewModel
) {
    // MainActivity 또는 상위 NavHost를 갖는 Composable에서 ViewModel 선언
    val cartItems = gptViewModel.cartItems.collectAsState().value
    val totalCount = cartItems.sumOf { it.count }
    val totalPrice = cartItems.sumOf { it.count * it.price.toInt() }

    val context = LocalContext.current

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
                Text(
                    text = if (isKorean) "주문 내역" else "Order Summary",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF5D2C15)
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (cartItems.isEmpty()) {
                    Text(
                        text = if (cartItems.isEmpty()) {
                            if (isKorean) "장바구니가 비어있습니다." else "Your cart is empty."
                        } else "",
                        color = Color.Gray
                    )
                } else {
                    cartItems.forEach { item ->
                        OrderItem(
                            name = item.name,
                            temp = "-", // 온도 제거했으므로 빈 값 처리
                            count = item.count,
                            price = item.price.toInt() * item.count,
                            isKorean = isKorean
                        )
                    }
                }

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
                Text(if (isKorean) "총 수량" else "Total Quantity", color = Color(0xFF5D2C15))
                Spacer(modifier = Modifier.height(4.dp))
                Text(if (isKorean) "총 결제금액" else "Total Price", color = Color(0xFF5D2C15))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$totalCount ${if (isKorean) "잔" else "cups"}",
                    color = Color(0xFF5D2C15)
                )
                Text(
                    text = "${"%,d".format(totalPrice)}${if (isKorean) "원" else "₩"}",
                    color = Color(0xFFE7662A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isKorean) "☕ 주문 내역을 확인해 주세요." else "☕ Please review your order.",
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
                onClick = { gptViewModel.clearCart()
                navController.popBackStack() },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCEBDD)),
                elevation = ButtonDefaults.buttonElevation(6.dp),
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
            ) {
                Text(
                    text = if (isKorean) "취소" else "Cancel",
                    fontSize = if (isKorean) 16.sp else 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            }

            // 🔸 주문 버튼 (강조 색상, 아이콘 포함)
            Button(
                onClick = {
                    if (cartItems.isEmpty()) {
                        Toast.makeText(
                            context,
                            if (isKorean) "장바구니가 비어있습니다." else "Your cart is empty.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        navController.navigate("phoneNumberInput")
                    }
                },
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5D2C15),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(56.dp)
                    .weight(2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.buy),
                    contentDescription = "장바구니",
                    modifier = Modifier.size(30.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isKorean) "주문하기" else "Order",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }


        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun OrderItem(name: String, temp: String, count: Int, price: Int, isKorean: Boolean) {
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

        Text(
            "$count ${if (isKorean) "잔" else "cups"}  ${"%,d".format(price)}${if (isKorean) "원" else "₩"}",
            fontSize = 14.sp
        )
    }
}

