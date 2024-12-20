package com.mercadolivro.respository.book

import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.resource.book.BookModel
import com.mercadolivro.resource.customer.CustomerModel
import org.springframework.data.repository.CrudRepository

interface  BookRepository: CrudRepository<BookModel,Int> {
    fun findByStatus(status: BooksStatus): List<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
}