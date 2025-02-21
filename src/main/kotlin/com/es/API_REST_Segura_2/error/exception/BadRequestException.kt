package com.es.API_REST_Segura_2.error.exception

class BadRequestException (message: String): Exception("Bad Request Exception (400). $message") {
}