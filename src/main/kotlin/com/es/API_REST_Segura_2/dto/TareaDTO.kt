package com.es.API_REST_Segura_2.dto


// Clase que representa una tarea en la base de datos este dto se usa para cuando devolvemos una tarea desde la api
data class TareaDTO(
    val titulo: String,
    val descripcion: String,
    val estado: Boolean
)