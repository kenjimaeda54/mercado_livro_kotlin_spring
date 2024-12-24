package com.mercadolivro.service.customer

import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.respository.customer.CustomerRepository
import com.mercadolivro.security.UserCustomDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserCustomDetailsService(
    private val customerRepository: CustomerRepository
): UserDetailsService {

    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt()).orElseThrow {
            NotFoundException(message = Errors.ML201.message.format(id), errorCode = Errors.ML201.code)
        }
      return UserCustomDetails(customer)
    }

}