package com.mercadolivro.controller.request.purchase

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.validation.book.ValidateExitBook
import com.mercadolivro.validation.book.ValidateStatusBook
import com.mercadolivro.validation.customer.ValidateCustomerId
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class PostPurchaseRequest (

    @field:NotNull
    @field:Positive(message = "Customer id have be more than 0")
    @ValidateCustomerId
    @JsonAlias("customer_id")
    val customerId: Int,

    //estou usando set pois se vier dois id igual o proprio set anula ele
    @field:NotNull
    @ValidateStatusBook
    @ValidateExitBook
    @JsonAlias("books_id")
    val booksId: Set<Int>
)
