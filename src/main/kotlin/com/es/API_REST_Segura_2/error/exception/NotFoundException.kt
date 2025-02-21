package com.es.API_REST_Segura_2.error.exception

class NotFoundException (message: String)
    : Exception("Not found exception (404). $message") {

}