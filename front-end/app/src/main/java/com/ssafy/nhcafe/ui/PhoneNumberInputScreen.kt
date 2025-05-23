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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.ui.common.TopBar

@Composable
fun PhoneNumberInputScreen(
    isKorean: Boolean,
    onLanguageToggle: () -> Unit,
    navController: NavController,
) {
    var phoneNumber by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            isKorean = isKorean,
            onLanguageToggle = onLanguageToggle
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = {navController.popBackStack()},
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cancel),
                    contentDescription = "닫기",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            painter = painterResource(id = R.drawable.phone),
            contentDescription = "휴대폰 아이콘",
            tint = Color(0xFFD28B36),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 제목 & 설명
        Text(
            text = if (isKorean) "전화번호 입력" else "Phone Number",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D2C15)
        )
        Text(
            text = if (isKorean) "스탬프 적립을 위해 전화번호를 입력해 주세요" else "Enter your number for stamp rewards",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = if (isKorean) "입력하지 않아도 주문이 가능해요" else "You can skip this step",
            fontSize = 12.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔢 입력창
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = {
                Text(
                    text = if (isKorean) "전화번호 (예: 01012345678)" else "Phone (e.g. 01012345678)",
                    color = Color.LightGray
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🙅‍♀️ 건너뛰기
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDE4D1)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (isKorean) "입력 안하고 계속하기" else "Continue without input",
                color = Color(0xFF5D2C15)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ 확인 버튼
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDFA878)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(
                text = if (isKorean) "확인" else "Confirm",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // ☕ 하단 문구
        Text(
            text = if (isKorean) "☕ 따뜻한 하루 되세요!" else "☕ Have a warm day!",
            fontSize = 12.sp,
            color = Color(0xFFB87D56)
        )
    }
}
