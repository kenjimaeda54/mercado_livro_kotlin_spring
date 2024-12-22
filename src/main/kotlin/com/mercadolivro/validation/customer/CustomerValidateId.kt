package com.mercadolivro.validation.customer

import com.mercadolivro.service.book.BookService
import com.mercadolivro.service.customer.CustomerService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

//eu  ConstraintValidator<ValidateCustomerId,Int> ao lado vai ser o tipo que vamos receber
//no caso aqui vaii ser o ID por isso e do tipo INt
class CustomerValidateId(private val customerService: CustomerService): ConstraintValidator<ValidateCustomerId,Int> {

    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return false
        }
        return customerService.existsCustomerId(value)
    }
}