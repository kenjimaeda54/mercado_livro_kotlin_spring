package com.mercadolivro.controller.request.book

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.controller.request.customer.ERROR_NAME
import com.mercadolivro.validation.customer.ValidateCustomerId
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

private const val NEED_PRICE = "Price can not null"
private const val CUSTOMER_ID = "CustomerId can not null"

data class  PostBookRequest(
    @field:NotEmpty(message = ERROR_NAME)
    val name: String,

    @field:NotNull(message = NEED_PRICE)
    val price: BigDecimal,

    @field:NotNull(message = CUSTOMER_ID)
    @ValidateCustomerId
    @JsonAlias("customer_id") //para lidar com nomes diferentes da requisição
    val customerId: Int,
)