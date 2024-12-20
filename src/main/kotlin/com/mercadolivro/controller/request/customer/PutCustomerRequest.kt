package com.mercadolivro.controller.request.customer


//estou constriundo as request separadas porue não posso ficar interferindo no model que seria nossas entidades
data class PutCustomerRequest(
    val name: String?,
    val email: String?
)
