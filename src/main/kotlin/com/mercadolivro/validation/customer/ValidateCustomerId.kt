package com.mercadolivro.validation.customer

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [CustomerValidateId::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ValidateCustomerId(
    val message: String = "Not exits customer with this id",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> =[]
)