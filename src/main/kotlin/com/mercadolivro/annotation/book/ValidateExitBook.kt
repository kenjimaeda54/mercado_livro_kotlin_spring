package com.mercadolivro.annotation.book

import com.mercadolivro.validation.book.BooksAvailableWithId
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [BooksAvailableWithId::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ValidateExitBook(
    val message: String = "Not found book with id solicited",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
