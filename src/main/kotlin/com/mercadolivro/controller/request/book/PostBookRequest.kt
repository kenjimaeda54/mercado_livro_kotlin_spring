package com.mercadolivro.controller.request.book

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal

data class  PostBookRequest(
    val name: String,
    val price: BigDecimal,
    @JsonAlias("customer_id") //para lidar com nomes diferentes da requisição
    val customerId: Int,
)