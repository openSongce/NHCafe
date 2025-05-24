package com.ssafy.nhcafe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
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
    var showNumpad by remember { mutableStateOf(false) }
    var displayPhoneNumber = formatPhoneNumber(phoneNumber)

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

        //  입력창
        PhoneNumberInputField(
            phoneNumber = displayPhoneNumber,
            isKorean = isKorean,
            onClick = { showNumpad = true }
        )


        Spacer(modifier = Modifier.height(16.dp))

        // ️ 건너뛰기
        Button(
            onClick = { navController.navigate("completeOrder/{orderNumber}")},
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

        // 확인 버튼
        Button(
            onClick = {if (phoneNumber.length == 11) {
                navController.navigate("stamp/${phoneNumber}")
            } },
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

        // 하단 문구
        Text(
            text = if (isKorean) "☕ 따뜻한 하루 되세요!" else "☕ Have a warm day!",
            fontSize = 12.sp,
            color = Color(0xFFB87D56)
        )
    }

    if (showNumpad) {
        Dialog(onDismissRequest = { showNumpad = false }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                CustomNumpad(
                    phoneNumber = displayPhoneNumber,
                    onInput = { digit ->
                        if (digit.all { it.isDigit() } && phoneNumber.length < 11) {
                            phoneNumber += digit
                        }
                    }
                    ,
                    onDelete = {
                        phoneNumber = phoneNumber.dropLast(1)
                    },
                    onClose = {
                        showNumpad = false
                    }
                )
            }
        }
    }
}

@Composable
fun PhoneNumberInputField(
    phoneNumber: String,
    isKorean: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = if (phoneNumber.isEmpty()) {
                if (isKorean) "전화번호 (예: 01012345678)" else "Phone (e.g. 01012345678)"
            } else {
                phoneNumber
            },
            color = if (phoneNumber.isEmpty()) Color.LightGray else Color.Black,
            fontSize = 16.sp
        )
    }

}

@Composable
fun CustomNumpad(
    phoneNumber: String,
    onInput: (String) -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // 🔢 입력값 표시 창
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFDE4D1), RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = phoneNumber,
                fontSize = 20.sp,
                color = Color(0xFF5D2C15)
            )
        }

        // 키패드 영역
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("←", "0", "완료")
        )

        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { key ->
                    Button(
                        onClick = {
                            when (key) {
                                "←" -> onDelete()
                                "완료" -> onClose()
                                else -> onInput(key)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCEBDD))
                    ) {
                        Text(
                            text = key,
                            fontSize = 19.sp,
                            color = Color(0xFF5D2C15)
                        )
                    }
                }
            }
        }
    }
}

fun formatPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }

    return when {
        digits.length <= 3 -> digits
        digits.length <= 7 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
        digits.length <= 11 -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
        else -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7, 11)}"
    }
}
