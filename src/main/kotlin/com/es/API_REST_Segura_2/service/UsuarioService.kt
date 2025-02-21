package com.es.API_REST_Segura_2.service

import com.es.API_REST_Segura_2.dto.UsuarioDTO
import com.es.API_REST_Segura_2.dto.UsuarioRegisterDTO
import com.es.API_REST_Segura_2.model.Usuario
import com.es.API_REST_Segura_2.repository.UsuarioRepository
import com.es.API_REST_Segura_2.error.exception.BadRequestException
import com.es.API_REST_Segura_2.error.exception.ConflictException
import com.es.API_REST_Segura_2.error.exception.UnauthorizedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var externalApiService: ExternalApiService


    override fun loadUserByUsername(username: String?): UserDetails {


        var usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username no existente")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build()
    }

    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO {

        // COMPROBACIONES
        // Comprobar si los campos vienen vacíos
        if (usuarioInsertadoDTO.username.isBlank()
            || usuarioInsertadoDTO.email.isBlank()
            || usuarioInsertadoDTO.password.isBlank()
            || usuarioInsertadoDTO.passwordRepeat.isBlank()) {

            throw BadRequestException("Uno o más campos vacíos")
        }

        if(usuarioRepository.findByUsername(usuarioInsertadoDTO.username).isPresent) {
            throw ConflictException("Usuario ${usuarioInsertadoDTO.username} ya está registrado")
        }
        if (usuarioRepository.findByEmail(usuarioInsertadoDTO.email).isPresent) {
            throw ConflictException("Email ${usuarioInsertadoDTO.email} ya está registrado")
        }

        if(usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat) {
            throw BadRequestException("Las contraseñas no coinciden")
        }

        if(usuarioInsertadoDTO.rol != null && usuarioInsertadoDTO.rol != "USER" && usuarioInsertadoDTO.rol != "ADMIN" ) {
            throw BadRequestException("ROL: ${usuarioInsertadoDTO.rol} incorrecto")
        }

        if(!usuarioInsertadoDTO.email.contains("@") || !usuarioInsertadoDTO.email.contains(".")) {
            throw BadRequestException("Verifique el email")
        }


        // Comprobar la provincia
        val datosProvincias = externalApiService.obtenerProvinciasDesdeApi()
        var cpro = ""
        if(datosProvincias != null) {
            if(datosProvincias.data != null) {
                val provinciaEncontrada = datosProvincias.data.stream().filter {
                    it.PRO == usuarioInsertadoDTO.direccion.provincia.uppercase()
                }.findFirst().orElseThrow {
                    BadRequestException("Provincia ${usuarioInsertadoDTO.direccion.provincia} no encontrada")
                }
                cpro = provinciaEncontrada.CPRO
            }
        }

        // Comprobar el municipio
        val datosMunicipios = externalApiService.obtenerMunicipiosDesdeApi(cpro)
        if(datosMunicipios != null) {
            if(datosMunicipios.data != null) {
                datosMunicipios.data.stream().filter {
                    it.DMUN50 == usuarioInsertadoDTO.direccion.municipio.uppercase()
                }.findFirst().orElseThrow {
                    BadRequestException("Municipio ${usuarioInsertadoDTO.direccion.municipio} incorrecto")
                }
            }
        }

        // Insertar el user (convierto a Entity)
        val usuario = Usuario(
            null,
            usuarioInsertadoDTO.username,
            passwordEncoder.encode(usuarioInsertadoDTO.password),
            usuarioInsertadoDTO.email,
            usuarioInsertadoDTO.rol,
            usuarioInsertadoDTO.direccion
        )

        // inserto en bd
        usuarioRepository.insert(usuario)

        // retorno un DTO
        return UsuarioDTO(
            usuario.username,
            usuario.email,
            usuario.roles
        )

    }

    // Funciones de gestión de usuarios, incorporadas por mí
    fun getAllUsers(): List<UsuarioDTO> {
        return usuarioRepository.findAll().map { usuario ->
            UsuarioDTO(
                usuario.username,
                usuario.email,
                usuario.roles
            )
        }
    }

    fun getUserByUsername(username: String): UsuarioDTO {
        val usuario = usuarioRepository.findByUsername(username).orElseThrow {
            UnauthorizedException("$username no existente")
        }
        return UsuarioDTO(
            usuario.username,
            usuario.email,
            usuario.roles
        )
    }

    fun updateUser(username: String, usuarioUpdateDTO: UsuarioRegisterDTO): UsuarioDTO {
        val usuario = usuarioRepository.findByUsername(
            username
        ).orElseThrow {
            UnauthorizedException("$username no existente")
        }

        val updatedUsuario = usuario.copy(
            username = usuarioUpdateDTO.username,
            password = passwordEncoder.encode(usuarioUpdateDTO.password),
            email = usuarioUpdateDTO.email,
            roles = usuarioUpdateDTO.rol,
            direccion = usuarioUpdateDTO.direccion
        )

        usuarioRepository.save(updatedUsuario)

        return UsuarioDTO(updatedUsuario.username, updatedUsuario.email, updatedUsuario.roles)
    }

    fun deleteUser(username: String) {
        val usuario = usuarioRepository.findByUsername(username).orElseThrow {
            UnauthorizedException("$username no existente")
        }
        usuarioRepository.delete(usuario)
    }
}