package com.es.API_REST_Segura_2.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document


@Document("tarea")
data class Tarea(
    @BsonId
    val _id: String?,

    val titulo: String,

    val descripcion: String,

    val estado: Boolean = false, // false = Pendiente, true = Completada

    val usuarioId: String // Identificador del usuario dueño de la tarea
)