package com.mercadolivro.controller.response.book

import com.fasterxml.jackson.annotation.JsonProperty
import com.mercadolivro.enums.BooksStatus
import java.math.BigDecimal

data class BookResponse(
    val id: Int? = null,
    val name: String,
    val price: BigDecimal,
    @JsonProperty("customer_id")//assim defindo o retorno como customer_id
    val customerId: Int,
    val status: BooksStatus? = null
)