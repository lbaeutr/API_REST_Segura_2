package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutinesScope = rememberCoroutineScope()
    val client = remember { HttpClient(CIO) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //todo incorporar imagen verificar eso de la imagen
//            Image(
//                painter = painterResource(R.drawable.logo),
//                contentDescription = "Logo",
//                modifier = Modifier.size(100.dp),
//                contentScale = ContentScale.Crop
//            )

            Text("Iniciar Sesión", style = MaterialTheme.typography.h5, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = nickname, onValueChange = { nickname = it },
                label = { Text("Nickname") },
                modifier = Modifier.background(Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = password, onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                coroutinesScope.launch {
                    val response: HttpResponse = client.post("http://localhost:8080/usuarios/login") {
                        contentType(ContentType.Application.Json)
                        setBody("""{"username": "$nickname", "password": "$password"}""")
                    }
                    when (response.status) {
                        HttpStatusCode.Created -> {
                            // TODO redirigir a pantalla de inicio
                            val responseBody = response.bodyAsText()
                            token = responseBody
                            println("Token JSON: $responseBody") // Imprime el token por terminal
                            errorMessage = "Inicio de sesión exitoso"
                        }
                        HttpStatusCode.Unauthorized -> {
                            errorMessage = "Credenciales incorrectas, verifica tus datos"
                        }
                        else -> {
                           // errorMessage = "Error desconocido: ${response.status}"
                            errorMessage = "Comprueba tu credenciales"
                        }
                    }
                }
            }) {
                Text("Ingresar")
            }
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = Color.Red)
            }

            if (token.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Token: $token", color = Color.Green)
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate", color = Color.White)
            }
        }
    }
}