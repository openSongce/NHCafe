package com.ssafy.nhcafe

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
import com.ssafy.nhcafe.ui.ConversationScreen
import com.ssafy.nhcafe.ui.MainScreen
import com.ssafy.nhcafe.ui.OrderConfirmScreen
import com.ssafy.nhcafe.ui.SplashScreen
import com.ssafy.nhcafe.ui.theme.NHCafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NHCafeTheme {
        Greeting("Android")
    }
}