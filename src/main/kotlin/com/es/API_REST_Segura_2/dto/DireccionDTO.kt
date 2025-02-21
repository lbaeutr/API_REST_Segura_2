package com.es.API_REST_Segura_2.dto


data class DireccionDTO(
    val calle: String,
    val num: String,
    val municipio: String,
    val provincia: String,
    val cp: String
)
