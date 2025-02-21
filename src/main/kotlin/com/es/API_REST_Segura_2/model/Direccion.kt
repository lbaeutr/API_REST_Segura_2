package com.es.API_REST_Segura_2.model


data class Direccion(
    val calle: String,
    val num: String,
    val municipio: String,
    val provincia: String,
    val cp: String
)
