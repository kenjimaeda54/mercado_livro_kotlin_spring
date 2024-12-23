package com.mercadolivro.extension.book

import com.mercadolivro.controller.response.book.BookResponse
import com.mercadolivro.resource.book.BookModel

fun BookModel.toResponse() = BookResponse(
    id = this.id,
    name = this.name,
    price = this.price,
    customerId = this.customer?.id ?: 0,
    status = this.status
)