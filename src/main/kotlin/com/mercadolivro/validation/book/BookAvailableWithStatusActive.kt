package com.mercadolivro.validation.book

import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.service.book.BookService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext


class BookAvailableWithStatusActive(
    private val bookService: BookService
): ConstraintValidator<ValidateStatusBook,Set<Int>> {

    override fun isValid(value: Set<Int>?, context: ConstraintValidatorContext?): Boolean {
        val books = value?.let {
            bookService.findAllBooksId(it)
        }
        if(books == null){
            return false
        }
        return books.firstOrNull {
            it.status == BooksStatus.DELETADO || it.status == BooksStatus.CANCELADO || it.status  == BooksStatus.VENDIDO
        } == null
    }

}