package com.es.API_REST_Segura_2.dto

import com.es.API_REST_Segura_2.model.Direccion

data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String?,
    val direccion: Direccion
)