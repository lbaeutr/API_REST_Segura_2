package com.es.API_REST_Segura_2.error.exception

class UnauthorizedException(message: String) : Exception("Not authorized exception (401). $message") {
}