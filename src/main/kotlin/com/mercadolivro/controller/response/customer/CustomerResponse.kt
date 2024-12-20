package com.mercadolivro.controller.response.customer

import com.mercadolivro.enums.CustomerStatus

data class CustomerResponse(
    val id: Int? = null,
    val name: String,
    val email: String,
    val status: CustomerStatus
)