package com.mercadolivro.mapper

import com.mercadolivro.controller.request.purchase.PostPurchaseRequest
import com.mercadolivro.resource.purchase.PurchaseModel
import com.mercadolivro.service.book.BookService
import com.mercadolivro.service.customer.CustomerService
import org.springframework.stereotype.Component

//assim deixo a injeção dependencia para o proprio spring
//se usasse extension eu não teria e precisaria pasar na mão para os contrutores

@Component
class PurchaseMapper(
    private  val bookService: BookService,
    private val customerService: CustomerService
) {

    fun toModel(request: PostPurchaseRequest): PurchaseModel  {
        val books = bookService.findAllBooksId(request.booksId)
        val customer = customerService.getOnlyCustomerById(request.customerId)

        return PurchaseModel(
            price = books.sumOf { it.price },
            customer = customer,
            books = books
        )
    }

}