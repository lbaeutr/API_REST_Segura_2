package com.es.API_REST_Segura_2.controller

import com.es.API_REST_Segura_2.dto.TareaCreateDTO
import com.es.API_REST_Segura_2.dto.TareaDTO
import com.es.API_REST_Segura_2.repository.UsuarioRepository
import com.es.API_REST_Segura_2.service.TareaService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tareas")
class TareaController {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    @Autowired
    private lateinit var tareaService: TareaService

    private fun obtenerUsuarioDesdeToken(request: HttpServletRequest): String {
        return request.userPrincipal?.name
            ?: throw RuntimeException("No se pudo obtener el usuario autenticado")
    }

    @PostMapping
    fun createTarea(
        request: HttpServletRequest,
        @RequestBody tareaCreateDTO: TareaCreateDTO
    ): ResponseEntity<TareaDTO> {
        val usuarioId = obtenerUsuarioDesdeToken(request)
        val tareaCreada = tareaService.createTarea(usuarioId, tareaCreateDTO)
        return ResponseEntity(tareaCreada, HttpStatus.CREATED)
    }

    @GetMapping
    fun getTareasByUsuario(request: HttpServletRequest): ResponseEntity<List<TareaDTO>> {
        val usuarioId = obtenerUsuarioDesdeToken(request)
        val tareas = tareaService.getTareasByUsuario(usuarioId)
        return ResponseEntity(tareas, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getTareaById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): ResponseEntity<TareaDTO> {
        val usuarioId = obtenerUsuarioDesdeToken(request)
        val tarea = tareaService.getTareaById(usuarioId, id)
        return ResponseEntity(tarea, HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateTarea(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody tareaUpdateDTO: TareaCreateDTO
    ): ResponseEntity<TareaDTO> {


        val usuarioId = obtenerUsuarioDesdeToken(request)
        val tareaActualizada = tareaService.updateTarea(usuarioId, id, tareaUpdateDTO)
        return ResponseEntity(tareaActualizada, HttpStatus.OK)
    }


    //borrar abajo

//    @PutMapping("/{id}")
//    fun updateTarea(
//        request: HttpServletRequest,
//        @PathVariable id: Long,
//        @RequestBody tareaUpdateDTO: TareaCreateDTO
//    ): ResponseEntity<TareaDTO> { //todo: ???
//
//
//        if (request.isUserInRole("ROLE_USER")) {
//            val usuarioId = obtenerUsuarioDesdeToken(request)
//            val tareaActualizada = tareaService.updateTarea(usuarioId, id, tareaUpdateDTO)
//            return ResponseEntity(tareaActualizada, HttpStatus.OK)
//        } else {
//            val tareasActualizadas = tareaService.updateAllTareas(obtenerUsuarioDesdeToken(request), listOf(tareaUpdateDTO))
//            return ResponseEntity(tareasActualizadas[id.toInt()], HttpStatus.OK)
//        }
//
//    }

//    @PutMapping("/{id}")
//    fun updateTarea(
//        @AuthenticationPrincipal userDetails: UserDetails,
//        @PathVariable id: Long,
//        @RequestBody tareaUpdateDTO: TareaCreateDTO
//    ): ResponseEntity<TareaDTO> {
//        println("🔍 Usuario: ${userDetails.username}, Rol: ${userDetails.authorities}")
//
//        val tareaActualizada = tareaService.updateTarea(userDetails.username, id, tareaUpdateDTO)
//        return ResponseEntity(tareaActualizada, HttpStatus.OK)
//    }

    @PutMapping("/mis-tareas")
    fun updateAllTareas(
        request: HttpServletRequest,
        @RequestBody tareasUpdateDTO: List<TareaCreateDTO>
    ): ResponseEntity<List<TareaDTO>> {
        val usuarioId = obtenerUsuarioDesdeToken(request)
        val tareasActualizadas = tareaService.updateAllTareas(usuarioId, tareasUpdateDTO)
        return ResponseEntity(tareasActualizadas, HttpStatus.OK)
    }


    @DeleteMapping("/{id}")
    fun deleteTarea(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): ResponseEntity<Void> {



        val usuarioId = obtenerUsuarioDesdeToken(request)
        tareaService.deleteTarea(usuarioId, id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

//    @DeleteMapping("/{id}")
//    fun deleteTarea(
//        request: HttpServletRequest,
//        @PathVariable id: Long
//    ): ResponseEntity<Void> {
//
//        if (request.isUserInRole("ROLE_USER")) {
//            val usuarioId = obtenerUsuarioDesdeToken(request)
//            tareaService.deleteTarea(usuarioId, id)
//            return ResponseEntity(HttpStatus.NO_CONTENT)
//        } else {
//            tareaService.deleteAnyTarea(id)
//            return ResponseEntity(HttpStatus.NO_CONTENT)
//        }
//    }


//    @GetMapping("/all")
//    fun getAllTareas(request: HttpServletRequest): ResponseEntity<List<TareaDTO>> {
//        val usuarioId = obtenerUsuarioDesdeToken(request)
//
//        // Verificar si el usuario es ADMIN
//        val esAdmin = request.isUserInRole("ROLE_ADMIN")
//
//        if (!esAdmin) {
//            return ResponseEntity(HttpStatus.FORBIDDEN)
//        }
//
//        val tareas = tareaService.getAllTareas()
//        return ResponseEntity(tareas, HttpStatus.OK)
//    }

    @GetMapping("/mis-tareas")
    fun getMisTareas(request: HttpServletRequest): ResponseEntity<List<TareaDTO>> {

        if (request.isUserInRole("ROLE_USER")) {
            val usuarioId = obtenerUsuarioDesdeToken(request)
            val tareas = tareaService.getTareasByUsuario(usuarioId)
            return ResponseEntity(tareas, HttpStatus.OK)
        } else {
            val tareas = tareaService.getAllTareas()
            return ResponseEntity(tareas, HttpStatus.OK)
        }
    }


    // Eliminar cualquier tarea de cualquier usuario (ADMIN)
    @DeleteMapping("/admin/{id}")
    fun deleteAnyTarea(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        val esAdmin = request.isUserInRole("ROLE_ADMIN")
        if (!esAdmin) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        tareaService.deleteAnyTarea(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    // Crear una tarea para cualquier usuario (ADMIN)
    @PostMapping("/admin")
    fun createTareaForUser(
        request: HttpServletRequest,
        @RequestBody tareaCreateDTO: TareaCreateDTO,
        @RequestParam usuarioId: String
    ): ResponseEntity<TareaDTO> {
        val esAdmin = request.isUserInRole("ROLE_ADMIN")
        val usuarioExiste = usuarioRepository.findByUsername(usuarioId).isPresent
        if (!esAdmin) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }


//        //todo: verificar que el usuario existe y comprobar que el codigo de error sea correcto
//        if (!usuarioExiste) {
//            return ResponseEntity(HttpStatus.NOT_FOUND)
//        }


        val tareaCreada = tareaService.createTareaForUser(usuarioId, tareaCreateDTO)
        return ResponseEntity(tareaCreada, HttpStatus.CREATED)
    }


}


