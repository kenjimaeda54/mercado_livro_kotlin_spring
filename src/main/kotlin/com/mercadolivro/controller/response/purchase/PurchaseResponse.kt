package com.mercadolivro.controller.response.purchase

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDateTime

data class PurchaseResponse (
    val id: Int,
    val price: BigDecimal,
    val createAt: LocalDateTime,
    val nfe: String,
    @JsonProperty("customer_id")
    val customerId: Int,
    @JsonProperty("books_id")
    val booksId:  MutableList<Int>
)