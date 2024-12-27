package com.mercadolivro.annotation.email

import com.mercadolivro.validation.email.EmailAvailableValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [EmailAvailableValidator::class])
@Retention(AnnotationRetention.RUNTIME)//quebrar em runtime ou seja no momento apos copilar codigo
@Target(AnnotationTarget.FIELD)//apenas nos metodos
annotation class EmailAvailable(
    val message: String = "Email exits", //message que vai estourar
    //abaixo e padrao
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
