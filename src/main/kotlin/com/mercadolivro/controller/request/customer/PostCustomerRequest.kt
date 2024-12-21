package com.mercadolivro.controller.request.customer

import com.mercadolivro.validation.email.EmailAvailable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

private const val ERROR_NAME =  "Name can not is empty"
private const val ERROR_MAIl = "Email need be valid"

data class PostCustomerRequest (

    @field:NotEmpty(message = ERROR_NAME)
    val name: String,

    @field:Email(message = ERROR_MAIl)
    //criei minha propria anotação
    @EmailAvailable
    val email: String
)