package com.mercadolivro.validation.book

import com.mercadolivro.annotation.book.ValidateExitBook
import com.mercadolivro.service.book.BookService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class BooksAvailableWithId(
    private val bookService: BookService
): ConstraintValidator<ValidateExitBook,Set<Int>> {

    override fun isValid(value: Set<Int>?, context: ConstraintValidatorContext?): Boolean {
        val books = value?.let {
           bookService.findAllBooksId(value)
        }
        if (books == null) {
            return false
        }
        return books.isNotEmpty()
    }

}