package com.ssafy.nhcafe

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ssafy.nhcafe.ui.CompleteOrderScreen
import com.ssafy.nhcafe.ui.ConversationScreen
import com.ssafy.nhcafe.ui.MainScreen
import com.ssafy.nhcafe.ui.MenuScreen
import com.ssafy.nhcafe.ui.OrderConfirmScreen
import com.ssafy.nhcafe.ui.PhoneNumberInputScreen
import com.ssafy.nhcafe.ui.SplashScreen
import com.ssafy.nhcafe.ui.StampScreen
import com.ssafy.nhcafe.ui.theme.NHCafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1
            )
        }

        setContent {
            val navController = rememberNavController()
            var isKorean by remember { mutableStateOf(true) }

            NavHost(navController, startDestination = "splash") {
                composable("splash") {
                    SplashScreen(navController)
                }
                composable("main") {
                    MainScreen(
                        navController = navController,
                        isKorean = isKorean,
                        onLanguageToggle = { isKorean = !isKorean }
                    )
                }
                composable("conversation") {
                    ConversationScreen(
                        navController = navController,
                        isKorean = isKorean,
                        onLanguageToggle = { isKorean = !isKorean })
                }
                composable("orderConfirm") {
                    OrderConfirmScreen(
                        navController = navController,
                        isKorean = isKorean,
                        onLanguageToggle = { isKorean = !isKorean }
                    )
                }
                composable("phoneNumberInput") {
                    PhoneNumberInputScreen(
                        isKorean = isKorean,                // 현재 언어 상태
                        onLanguageToggle = { isKorean = !isKorean },  // 언어 토글
                        navController = navController, // 닫기 버튼 (뒤로가기)
                    )
                }
                composable("stamp/{phoneNumber}") { backStackEntry ->
                    val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
                    StampScreen(
                        navController = navController,
                        isKorean = isKorean,
                        onLanguageToggle = { isKorean = !isKorean },
                        phoneNumber = phoneNumber
                    )
                }
                composable("completeOrder/{orderNumber}") { backStackEntry ->
                    val orderNumber = backStackEntry.arguments?.getString("orderNumber")?.toIntOrNull() ?: 0
                    CompleteOrderScreen(
                        navController = navController,
                        isKorean = isKorean,
                        onLanguageToggle = { isKorean = !isKorean },
                        orderNumber = orderNumber
                    )
                }
                composable("menu") {
                    MenuScreen(
                        navController = navController,
                        isKorean = isKorean,
                        onLanguageToggle = { isKorean = !isKorean }
                    )
                }





            }
        }
    }
}
