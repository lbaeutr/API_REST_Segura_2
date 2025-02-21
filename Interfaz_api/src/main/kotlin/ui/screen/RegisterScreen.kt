
package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    //var rol by remember { mutableStateOf("USER") }
    var provincia by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val client = remember { HttpClient(CIO) }
    var errorColor by remember { mutableStateOf(Color.Red) }

    // FocusRequester para cada campo de texto que este vacio --> para que se ponga el foco en el campo vacio
    val usernameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val passwordRepeatFocusRequester = remember { FocusRequester() }
    val provinciaFocusRequester = remember { FocusRequester() }
    val municipioFocusRequester = remember { FocusRequester() }
    val calleFocusRequester = remember { FocusRequester() }
    val numFocusRequester = remember { FocusRequester() }
    val cpFocusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Black)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro", style = MaterialTheme.typography.h5, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Column(modifier = Modifier.weight(1f)) {

                    Text("Datos usuario", style = MaterialTheme.typography.h6, color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(value = username, onValueChange = { username = it },
                        label = { Text("Nombre de Usuario") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(usernameFocusRequester)
                    )


                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = email, onValueChange = { email = it },
                        label = { Text("Correo Electrónico") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(emailFocusRequester)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = password, onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = passwordRepeat, onValueChange = { passwordRepeat = it },
                        label = { Text("Confirmar Contraseña") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(passwordRepeatFocusRequester)
                    )

//                    Spacer(modifier = Modifier.height(8.dp))
//                    TextField(value = rol, onValueChange = { rol = it },
//                        label = { Text("Rol") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Datos domicilio usuario", style = MaterialTheme.typography.h6, color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = provincia, onValueChange = { provincia = it },
                        label = { Text("Provincia") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(provinciaFocusRequester)
                    )


                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = municipio, onValueChange = { municipio = it },
                        label = { Text("Municipio") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(municipioFocusRequester)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = calle, onValueChange = { calle = it },
                        label = { Text("Calle") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(calleFocusRequester)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = num, onValueChange = { num = it },
                        label = { Text("Número") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(numFocusRequester)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = cp, onValueChange = { cp = it },
                        label = { Text("Código Postal") },
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .focusRequester(cpFocusRequester)
                    )
                }
            }




            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                when {
                    username.isEmpty() -> {
                        errorMessage = "El nombre de usuario no puede estar vacío"
                        usernameFocusRequester.requestFocus()
                    }
                    email.isEmpty() -> {
                        errorMessage = "El correo electrónico no puede estar vacío"
                        emailFocusRequester.requestFocus()
                    }
                    password.isEmpty() -> {
                        errorMessage = "La contraseña no puede estar vacía"
                        passwordFocusRequester.requestFocus()
                    }
                    passwordRepeat.isEmpty() -> {
                        errorMessage = "La confirmación de la contraseña no puede estar vacía"
                        passwordRepeatFocusRequester.requestFocus()
                    }
                    password != passwordRepeat -> {
                        errorMessage = "Las contraseñas no coinciden"
                        passwordFocusRequester.requestFocus()
                    }
                    provincia.isEmpty() -> {
                        errorMessage = "La provincia no puede estar vacía"
                        provinciaFocusRequester.requestFocus()
                    }
                    municipio.isEmpty() -> {
                        errorMessage = "El municipio no puede estar vacío"
                        municipioFocusRequester.requestFocus()
                    }
                    calle.isEmpty() -> {
                        errorMessage = "La calle no puede estar vacía"
                        calleFocusRequester.requestFocus()
                    }
                    num.isEmpty() -> {
                        errorMessage = "El número no puede estar vacío"
                        numFocusRequester.requestFocus()
                    }
                    cp.isEmpty() -> {
                        errorMessage = "El código postal no puede estar vacío"
                        cpFocusRequester.requestFocus()
                    }
                    else -> {
                        coroutineScope.launch {
                            val response: HttpResponse = client.post("http://localhost:8080/usuarios/register") {
                                contentType(ContentType.Application.Json)
                                setBody(
                                    """{
                                        "username": "$username",
                                        "email": "$email",
                                        "password": "$password",
                                        "passwordRepeat": "$passwordRepeat",
                                        "rol": "USER",
                                        "direccion": {
                                            "provincia": "$provincia",
                                            "municipio": "$municipio",
                                            "calle": "$calle",
                                            "num": "$num",
                                            "cp": "$cp"
                                        }
                                    }"""
                                )
                            }
                            when (response.status) {
                                HttpStatusCode.Created -> {
                                    errorMessage = "Registro exitoso"
                                    errorColor = Color.Green
                                }
                                else -> {
                                   val errorBody = response.bodyAsText()
                                    //errorMessage = "Error en el registro: $errorResponse"
                                    errorMessage = if (errorBody.isNotBlank()) errorBody else response.status.description
                                }
                            }
                        }
                    }
                }
            }) {
                Text("Registrarse", color = Color.White)
            }
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = errorColor)
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color.White)
            }
        }

    }

}
