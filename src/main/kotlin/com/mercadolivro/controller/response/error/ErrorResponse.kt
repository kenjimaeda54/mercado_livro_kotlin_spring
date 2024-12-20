package com.mercadolivro.controller.response.error

data class ErrorResponse (
    val httpStatusCode: Int,
    val errorCode: String,
    val message: String,
    val fieldErrors: List<FieldErrors>?
)