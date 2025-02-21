package com.es.API_REST_Segura_2.controller

import com.es.API_REST_Segura_2.dto.LoginUsuarioDTO
import com.es.API_REST_Segura_2.dto.UsuarioDTO
import com.es.API_REST_Segura_2.dto.UsuarioRegisterDTO
import com.es.API_REST_Segura_2.error.exception.BadRequestException
import com.es.API_REST_Segura_2.error.exception.MissingUsernamePassException
import com.es.API_REST_Segura_2.service.TokenService
import com.es.API_REST_Segura_2.service.UsuarioService
import com.es.API_REST_Segura_2.error.exception.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @Autowired
    private lateinit var tokenService: TokenService
    @Autowired
    private lateinit var usuarioService: UsuarioService

    @PostMapping("/register")
    fun insert(
        httpRequest: HttpServletRequest,
        @RequestBody usuarioRegisterDTO: UsuarioRegisterDTO
    ) : ResponseEntity<UsuarioDTO>{

        val usuarioInsertadoDTO: UsuarioDTO = usuarioService.insertUser(usuarioRegisterDTO)

        return ResponseEntity(usuarioInsertadoDTO, HttpStatus.CREATED)

    }

    @PostMapping("/login")
    fun login(@RequestBody usuario: LoginUsuarioDTO) : ResponseEntity<Any>? {
        if (usuario.username.isBlank() || usuario.password.isBlank()) {
            throw BadRequestException("El usuario o la contraseña no pueden estar vacíos")
        }

        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Credenciales incorrectas")

        }

        // SI PASAMOS LA AUTENTICACIÓN, SIGNIFICA QUE ESTAMOS BIEN AUTENTICADOS
        // PASAMOS A GENERAR EL TOKEN
        var token = tokenService.generarToken(authentication)

        return ResponseEntity(mapOf("token" to token), HttpStatus.CREATED)
    }




    // Obtener todos los usuarios
    @GetMapping("/all")
    fun getAllUsers(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = usuarioService.getAllUsers()
        return ResponseEntity(usuarios, HttpStatus.OK)
    }

    // Obtener un usuario por su username
    @GetMapping("/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioService.getUserByUsername(username)
        return ResponseEntity(usuario, HttpStatus.OK)
    }

    // Actualizar un usuario
    @PutMapping("/{username}")
    fun updateUser(
        @PathVariable username: String,
        @RequestBody usuarioUpdateDTO: UsuarioRegisterDTO
    ): ResponseEntity<UsuarioDTO> {
        val updatedUsuario = usuarioService.updateUser(username, usuarioUpdateDTO)
        return ResponseEntity(updatedUsuario, HttpStatus.OK)
    }

    // Eliminar un usuario
    @DeleteMapping("/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Void> {
        usuarioService.deleteUser(username)
        return ResponseEntity(HttpStatus.OK)
    }
}