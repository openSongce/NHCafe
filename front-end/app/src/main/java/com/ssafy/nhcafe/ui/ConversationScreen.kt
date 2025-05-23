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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.nhcafe.R
import com.ssafy.nhcafe.ui.common.TopBar
import kotlinx.coroutines.delay

@Composable
fun ConversationScreen(navController: NavController,
                       isKorean: Boolean,
                       onLanguageToggle: () -> Unit) {
    var isListening by remember { mutableStateOf(false) }


    val messages = listOf(
        Message("아이스 아메리카노 한 잔이요.", true),
        Message("아이스 아메리카노 한 잔, 맞으실까요?", false),
        Message("따뜻한 카페라떼 두 잔이요.", true),
        Message("카페라떼 두 잔 추가하겠습니다. 맞으실까요?", false),
        Message("네, 맞아요.", true),
        Message("추가로 원하시는 게 있으신가요?", false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF5ED))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEF5ED))
                .padding(horizontal = 20.dp)

        ){
            TopBar(isKorean = isKorean,
                onLanguageToggle = onLanguageToggle)
        }

        Text(
            text = if (isKorean) "천천히 또박또박 말씀해 주세요." else "Please speak slowly and clearly.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.size) { index ->
                ChatBubble(messages[index])
            }

            // 👇 듣고 있어요 삽입
            if (isListening) {
                item {
                    ListeningIndicator()
                }
            }
        }


        BottomButtonBar(isListening = isListening,
            onMicClick = { isListening = true}, onOrderClick = {
                navController.navigate("orderConfirm")
            })
    }

    LaunchedEffect(isListening) {
        if (isListening) {
            delay(3000L)
            isListening = false
        }
    }

}


data class Message(val text: String, val isUser: Boolean)


@Composable
fun ChatBubble(message: Message) {
    val backgroundColor = if (message.isUser) Color(0xFFFFC074) else Color(0xFFFCEBDD)
    val alignment = if (message.isUser) Arrangement.End else Arrangement.Start
    val avatar = if (message.isUser) R.drawable.user_avatar else R.drawable.bot_avatar

    Row(
        horizontalArrangement = alignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!message.isUser) {
            Image(
                painter = painterResource(id = avatar),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color(0x33000000), // 부드러운 회색 그림자
                    spotColor = Color(0x55000000)     // 그림자 강조 색
                )
                .widthIn(max = 280.dp)
        ) {
            Text(message.text, fontSize = 14.sp)
        }

        if (message.isUser) {
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(id = avatar),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
        }
    }
}


@Composable
fun BottomButtonBar(isListening: Boolean,
                    onMicClick: () -> Unit, onOrderClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEF5ED))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onMicClick,  // ✅ 여기!
            shape = CircleShape,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDFA878)),
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.mic),
                contentDescription = "Mic Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("말하기")
        }


        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { onOrderClick() },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .height(60.dp)
                .weight(1f),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5D2C15),
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.shoppingcart),
                contentDescription = "cart Icon",

                modifier = Modifier.size(40.dp).clip(CircleShape) // 아이콘 크기 조정
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("주문하기")
        }
    }
}

@Composable
fun ListeningIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.listening_icon), // 이미지 파일 이름
            contentDescription = "듣는 중",
            modifier = Modifier.size(180.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}




