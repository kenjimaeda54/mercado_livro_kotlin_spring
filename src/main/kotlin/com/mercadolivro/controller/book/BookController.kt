package com.mercadolivro.controller.book

import com.mercadolivro.controller.request.book.PostBookRequest
import com.mercadolivro.controller.request.book.PutBookRequest
import com.mercadolivro.controller.response.book.BookResponse
import com.mercadolivro.extension.book.toModel
import com.mercadolivro.extension.book.toResponse
import com.mercadolivro.service.book.BookService
import com.mercadolivro.service.customer.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private const val REQUEST_MAPPING = "books"

@RestController
@RequestMapping(REQUEST_MAPPING)
class BookController(
    val customerService: CustomerService,
    val bookService: BookService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody @Valid book: PostBookRequest) {
        val customer = customerService.getOnlyCustomerById(book.customerId)
        bookService.createBook(book.toModel(customer))
    }

    @GetMapping
    fun findAllBook(@PageableDefault(size = 10, page = 0) pageable: Pageable): Page<BookResponse> =
        bookService.findAllBook(
           pageable
        ).map { it.toResponse() }

    @GetMapping("/actives")
    fun findBookActives(@PageableDefault(size = 10, page = 0) pageable: Pageable): Page
    <BookResponse> = bookService.findActives(pageable).map { it.toResponse() }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): BookResponse = bookService.findById(id).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) = bookService.deleteBook(id)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateBook(@RequestBody book: PutBookRequest, @PathVariable id: Int) {
        val bookSaved = bookService.findById(id) //precisa no mapper passar id para atualiizar
        //nao existe  o metodo update no jpa
        bookService.updateBook(book.toModel(bookSaved))
    }
}