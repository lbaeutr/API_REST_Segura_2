package com.es.API_REST_Segura_2.model

import org.springframework.data.mongodb.core.mapping.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.annotation.Id

@Document("tarea")
data class Tarea(
    @BsonId @Id
    val _id: Long,
    val titulo: String,
    val descripcion: String,
    val estado: Boolean = false,
    val usuarioId: String
)
