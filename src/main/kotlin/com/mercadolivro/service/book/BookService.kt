package com.mercadolivro.service.book

import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.resource.book.BookModel
import com.mercadolivro.resource.customer.CustomerModel
import com.mercadolivro.respository.book.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun createBook(bookModel: BookModel) = bookRepository.save(bookModel)

    fun findAllBook(): List<BookModel> = bookRepository.findAll().toList()
    
    fun findActives(): List<BookModel> = bookRepository.findByStatus(BooksStatus.ATIVO)

    fun findById(id: Int): BookModel = bookRepository.findById(id).orElseThrow()

    fun deleteBook(id: Int)  {
       val book = findById(id)
       book.status = BooksStatus.CANCELADO
       updateBook(book)
    }

    fun updateBook(bookModel: BookModel) = bookRepository.save(bookModel)

    fun deleteBooksByCustomer(customerModel: CustomerModel){
        val books = bookRepository.findByCustomer(customerModel)
        //lembrando que um customer pode ter varios livros
        for(book in books) {
            book.status = BooksStatus.DELETADO
        }
        bookRepository.saveAll(books)
    }
}