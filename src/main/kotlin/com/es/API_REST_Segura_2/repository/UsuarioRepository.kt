package com.es.API_REST_Segura_2.repository

import com.es.API_REST_Segura_2.model.Tarea
import com.es.API_REST_Segura_2.model.Usuario
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsuarioRepository : MongoRepository<Usuario, String> {

    fun findByUsername(username: String): Optional<Usuario>

    fun findByEmail(email: String): Optional<Usuario>

}