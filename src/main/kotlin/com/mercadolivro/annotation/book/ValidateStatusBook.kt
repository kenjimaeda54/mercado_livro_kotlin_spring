package com.mercadolivro.annotation.book

import com.mercadolivro.validation.book.BookAvailableWithStatusActive
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [BookAvailableWithStatusActive::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ValidateStatusBook(
    val message: String = "Books only with status ATIVO can be sold",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)