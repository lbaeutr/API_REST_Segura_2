package com.es.API_REST_Segura_2.dto

data class TareaCreateDTO(
    val titulo: String,
    val descripcion: String,
    val estado: Boolean,
)