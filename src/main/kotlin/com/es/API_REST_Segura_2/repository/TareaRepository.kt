package com.es.API_REST_Segura_2.repository

import com.es.API_REST_Segura_2.model.Tarea
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TareaRepository : MongoRepository<Tarea, Long> {
    fun findByUsuarioId(usuarioId: String): List<Tarea>
}