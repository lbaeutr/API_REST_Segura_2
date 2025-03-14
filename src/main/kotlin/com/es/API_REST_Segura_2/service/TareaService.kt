package com.es.API_REST_Segura_2.service

import com.es.API_REST_Segura_2.dto.TareaCreateDTO
import com.es.API_REST_Segura_2.dto.TareaDTO
import com.es.API_REST_Segura_2.error.exception.BadRequestException
import com.es.API_REST_Segura_2.error.exception.UnauthorizedException
import com.es.API_REST_Segura_2.model.Tarea
import com.es.API_REST_Segura_2.repository.TareaRepository
import com.es.API_REST_Segura_2.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TareaService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    @Autowired
    private lateinit var tareaRepository: TareaRepository

    // Crear una nueva tarea con ID autoincremental
    fun createTarea(usuarioId: String, tareaCreateDTO: TareaCreateDTO): TareaDTO {
        if (tareaCreateDTO.titulo.isBlank() || tareaCreateDTO.descripcion.isBlank()) {
            throw BadRequestException("El título y la descripción no pueden estar vacíos")
        }

        // Obtener el último ID y sumarle 1
        val lastTarea = tareaRepository.findAll().maxByOrNull { it._id }
        val nextId = (lastTarea?._id ?: 0) + 1 // Si no hay tareas, empieza en 1

        val nuevaTarea = Tarea(
            _id = nextId,
            titulo = tareaCreateDTO.titulo,
            descripcion = tareaCreateDTO.descripcion,
            estado = false, // Siempre comienza en "pendiente"
            usuarioId = usuarioId
        )

        tareaRepository.insert(nuevaTarea)

        return TareaDTO(
            _id = nuevaTarea._id.toString(),
            titulo = nuevaTarea.titulo,
            descripcion = nuevaTarea.descripcion,
            estado = nuevaTarea.estado,
            usuarioId = nuevaTarea.usuarioId
        )
    }

    // Obtener todas las tareas de un usuario
    fun getTareasByUsuario(usuarioId: String): List<TareaDTO> {
        val tareas = tareaRepository.findByUsuarioId(usuarioId)
        return tareas.map { tarea ->
            TareaDTO(
                _id = tarea._id.toString(),
                titulo = tarea.titulo,
                descripcion = tarea.descripcion,
                estado = tarea.estado,
                usuarioId = tarea.usuarioId
            )
        }
    }


    // Obtener una tarea específica
    fun getTareaById(usuarioId: String, tareaId: Long): TareaDTO {
        val tarea = tareaRepository.findById(tareaId).orElseThrow {
            BadRequestException("Tarea no encontrada")
        }

        // Verificar que el usuario solo pueda ver sus propias tareas
        if (tarea.usuarioId != usuarioId) {
            throw UnauthorizedException("No puedes acceder a esta tarea")
        }

        return TareaDTO(tarea._id.toString(), tarea.titulo, tarea.descripcion, tarea.estado, tarea.usuarioId)
    }

    // Actualizar una tarea
    fun updateTarea(usuarioId: String, tareaId: Long, tareaUpdateDTO: TareaCreateDTO): TareaDTO {
        val tarea = tareaRepository.findById(tareaId).orElseThrow {
            BadRequestException("Tarea no encontrada")
        }

        // Verificar que el usuario solo pueda modificar sus propias tareas
        if (tarea.usuarioId != usuarioId) {
            throw UnauthorizedException("No puedes modificar esta tarea")
        }

        // Verificar que el título y la descripción no estén vacíos
        if (tareaUpdateDTO.titulo.isBlank() || tareaUpdateDTO.descripcion.isBlank()) {
            throw BadRequestException("El título y la descripción no pueden estar vacíos")
        }

        val tareaActualizada = tarea.copy(
            titulo = tareaUpdateDTO.titulo,
            descripcion = tareaUpdateDTO.descripcion,
            estado = tareaUpdateDTO.estado,

        )

        tareaRepository.save(tareaActualizada)

        return TareaDTO(
            tareaActualizada._id.toString(),
            tareaActualizada.titulo,
            tareaActualizada.descripcion,
            tareaActualizada.estado,
            tareaActualizada.usuarioId
        )
    }


    // Actualizar todas las tareas de un usuario
    fun updateAllTareas(usuarioId: String, tareasUpdateDTO: List<TareaCreateDTO>): List<TareaDTO> {
        val tareasUsuario = tareaRepository.findByUsuarioId(usuarioId)

        if (tareasUsuario.isEmpty()) {
            throw BadRequestException("No tienes tareas para actualizar")
        }

        /*
        Se recorre tareasUsuario con mapIndexed, obteniendo el índice y la tarea.
        Luego, se busca el TareaCreateDTO correspondiente en tareasUpdateDTO.
        Si no existe (getOrNull(index) == null), se mantiene la tarea original.
         */
        val tareasActualizadas = tareasUsuario.mapIndexed { index, tarea ->
            val updateDTO = tareasUpdateDTO.getOrNull(index) ?: return@mapIndexed tarea
            tarea.copy(
                titulo = updateDTO.titulo,
                descripcion = updateDTO.descripcion,
                estado = updateDTO.estado,
            )
        }

        tareaRepository.saveAll(tareasActualizadas)

        return tareasActualizadas.map { tarea ->
            TareaDTO(tarea._id.toString(), tarea.titulo, tarea.descripcion, tarea.estado, tarea.usuarioId)
        }
    }


    // Eliminar una tarea
    fun deleteTarea(usuarioId: String, tareaId: Long) {


        // Verificar que la tarea exista
        val tarea = tareaRepository.findById(tareaId).orElseThrow {
            BadRequestException("Tarea no encontrada")
        }

        // Verificar que el usuario solo pueda eliminar sus propias tareas
        if (tarea.usuarioId != usuarioId) {
            throw UnauthorizedException("No puedes eliminar esta tarea")
        }

        tareaRepository.delete(tarea)
    }

    // Obtener todas las tareas de todos los usuarios
    fun getAllTareas(): List<TareaDTO> {
        val tareas = tareaRepository.findAll()
        return tareas.map { tarea ->
            TareaDTO(tarea._id.toString(), tarea.titulo, tarea.descripcion, tarea.estado, tarea.usuarioId)
        }
    }


    // Eliminar cualquier tarea de cualquier usuario (ADMIN)
    fun deleteAnyTarea(tareaId: Long) {
        val tarea = tareaRepository.findById(tareaId).orElseThrow {
            BadRequestException("Tarea no encontrada")
        }
        tareaRepository.delete(tarea)
    }




}
