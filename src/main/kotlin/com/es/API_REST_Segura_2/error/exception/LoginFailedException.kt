package com.es.API_REST_Segura_2.error.exception

class LoginFailedException(message: String) : Exception("Login failed: $message")