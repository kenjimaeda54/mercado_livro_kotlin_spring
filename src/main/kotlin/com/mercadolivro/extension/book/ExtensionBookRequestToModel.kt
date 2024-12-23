package com.mercadolivro.extension.book

import com.mercadolivro.controller.request.book.PostBookRequest
import com.mercadolivro.controller.request.book.PutBookRequest
import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.resource.book.BookModel
import com.mercadolivro.resource.customer.CustomerModel

fun PostBookRequest.toModel(customerModel: CustomerModel) = BookModel(
    name = this.name,
    price = this.price,
    status = BooksStatus.ATIVO,
    customer = customerModel
)

fun PutBookRequest.toModel(previous: BookModel) = BookModel(
    id = previous.id,
    name = this.name ?: previous.name,
    price = this.price ?: previous.price,
    customer = previous.customer,
    status = previous.status
)