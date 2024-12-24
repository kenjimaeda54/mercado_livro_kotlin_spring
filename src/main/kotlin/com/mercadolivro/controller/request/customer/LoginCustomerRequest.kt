package com.mercadolivro.controller.request.customer

import jakarta.validation.constraints.Email
import com.mercadolivro.controller.request.customer.ERROR_MAIl
import jakarta.validation.constraints.NotEmpty



data class LoginCustomerRequest (
    @field:Email(message = ERROR_MAIl)
    val email: String,

    @field:NotEmpty(message = PASSWORD_REQUIRED)
    val password: String
)