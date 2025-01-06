package com.mercadolivro.respository.customer

import com.mercadolivro.resource.customer.CustomerModel
import org.springframework.data.repository.CrudRepository

//espera a entity e o tipo do ID
interface CustomerRepository: CrudRepository<CustomerModel,Int>  {
    fun findByNameContaining(name: String): List<CustomerModel>
    fun existsByEmail(value: String): Boolean
    fun findByEmail(email: String): CustomerModel?
}