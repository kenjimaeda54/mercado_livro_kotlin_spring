package com.mercadolivro.controller.request.customer

import com.mercadolivro.annotation.email.EmailAvailable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

const val ERROR_NAME =  "Name can not is empty"
const val ERROR_MAIl = "Email need be valid"
const val PASSWORD_REQUIRED = "Password is required to create customer"

data class PostCustomerRequest (

    @field:NotEmpty(message = ERROR_NAME)
    val name: String,

    @field:NotEmpty(message = PASSWORD_REQUIRED)
    val password: String,

    @field:Email(message = ERROR_MAIl)
    //criei minha propria anotação
    @EmailAvailable
    val email: String
)