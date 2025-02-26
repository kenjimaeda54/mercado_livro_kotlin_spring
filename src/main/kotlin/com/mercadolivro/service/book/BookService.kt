package com.mercadolivro.service.book

import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.resource.book.BookModel
import com.mercadolivro.resource.customer.CustomerModel
import com.mercadolivro.respository.book.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun createBook(bookModel: BookModel) = bookRepository.save(bookModel)

    fun findAllBook(pageable: Pageable): Page<BookModel> = bookRepository.findAll(pageable)

    fun findActives(pageable: Pageable): Page<BookModel> = bookRepository.findByStatus(BooksStatus.ATIVO, pageable)

    // Error code pode ser um erro interno
    // assiim fica facil identificar o motivo do erro
    fun findById(id: Int): BookModel = bookRepository.findById(id)
        .orElseThrow { NotFoundException(message = Errors.ML101.message.format(id), errorCode = Errors.ML101.code) }

    fun deleteBook(id: Int) {
        val book = findById(id)
        book.status = BooksStatus.CANCELADO
        updateBook(book)
    }

    fun updateBook(bookModel: BookModel) = bookRepository.save(bookModel)

    fun deleteBooksByCustomer(customerModel: CustomerModel) {
        val books = bookRepository.findByCustomer(customerModel)
        //lembrando que um customer pode ter varios livros
        for (book in books) {
            book.status = BooksStatus.DELETADO
        }
        bookRepository.saveAll(books)
    }

    fun findAllBooksId(booksId: Set<Int>): MutableList<BookModel> = bookRepository.findAllById(booksId)
    
    fun booksPurchase(books: MutableList<BookModel>): MutableList<BookModel> = bookRepository.saveAll(books)

}