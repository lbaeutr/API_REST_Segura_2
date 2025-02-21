package com.es.API_REST_Segura_2.error.exception

class ConflictException(message: String) : Exception("Conflict (409): $message")