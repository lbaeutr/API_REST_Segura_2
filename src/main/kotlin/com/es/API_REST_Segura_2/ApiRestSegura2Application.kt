package com.es.API_REST_Segura_2

import com.es.API_REST_Segura_2.security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class ApiRestSegura2Application

fun main(args: Array<String>) {
	runApplication<ApiRestSegura2Application>(*args)
}
