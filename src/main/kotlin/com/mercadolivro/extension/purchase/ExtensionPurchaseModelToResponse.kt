package com.mercadolivro.extension.purchase

import com.mercadolivro.controller.response.purchase.PurchaseResponse
import com.mercadolivro.resource.purchase.PurchaseModel

fun PurchaseModel.toResponse(): PurchaseResponse = PurchaseResponse(
    id = this.id ?: 0,
    price = this.price,
    createAt = this.createAt,
    nfe = this.nfe ?: "",
    customerId = this.customer?.id ?: 0,
    booksId = this.books.map { it.id ?: 0 }.toMutableList()
)