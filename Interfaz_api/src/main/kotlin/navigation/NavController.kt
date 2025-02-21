package navigation

import androidx.compose.runtime.*
import ui.screens.LoginScreen
import ui.screens.RegisterScreen

@Composable
fun NavController() {
    var currentScreen by remember { mutableStateOf("login") }

    when (currentScreen) {
        "login" -> LoginScreen(onNavigateToRegister = { currentScreen = "register" })
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen = "login" })
    }
}
