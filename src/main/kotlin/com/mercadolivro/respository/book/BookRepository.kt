package com.mercadolivro.respository.book

import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.resource.book.BookModel
import com.mercadolivro.resource.customer.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository


//posso extender tanto de CrudRepository
//como JPARepository
//paaraa lidar com paginação idela e o JPARepository
interface  BookRepository: JpaRepository<BookModel,Int> {
    fun findByStatus(status: BooksStatus,pageable: Pageable): Page<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
}