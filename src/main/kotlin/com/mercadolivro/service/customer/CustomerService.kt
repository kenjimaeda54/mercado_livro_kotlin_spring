package com.mercadolivro.service.customer

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.ProfileRoles
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.resource.customer.CustomerModel
import com.mercadolivro.respository.customer.CustomerRepository
import com.mercadolivro.service.book.BookService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository, //injeção dependencia
    val bookService: BookService,
    val bcrypt: BCryptPasswordEncoder
) {
    //jpa ja tem varios metodos disponiveis para acessar abaixo todos os metodos diponiveis
    //no jpaa
    fun getAllUsers(name: String?): List<CustomerModel> {
        name?.let {
            //esttou usando contains para ficar maais flexivel
            //pois com contain consigo retornar não apenas o valor literal,
            //maas tambem minusculo,maisculo e casos parecidos tipo se a pessoa inserir a
            //lembrnado que name sera o valor apos o ?name
            return customerRepository.findByNameContaining(name)
        }
        return customerRepository.findAll().toList()
    }

    //camada service nao pode receber as request por isso
    //criei umaa camada extension paara mapear esse model
    fun createUser(customer: CustomerModel) {
        val newCustomer = customer.copy(
            role = setOf(ProfileRoles.CUSTOMER),
            password = bcrypt.encode(customer.password)
        )
        customerRepository.save(newCustomer)
    }

    fun getOnlyCustomerById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow {
            NotFoundException(message = Errors.ML201.message.format(id), errorCode = Errors.ML201.code)
        }
    }

    fun updateUser(customer: CustomerModel) {
        customer.id?.let {
            if (!customerRepository.existsById(customer.id)) {
                throw  NotFoundException(message = Errors.ML201.message.format(customer.id), errorCode = Errors.ML201.code)
            }
            customerRepository.save(customer)
        }

    }

    fun deleteUser(id: Int) {
        if (!customerRepository.existsById(id)) {
            throw throw  NotFoundException(message = Errors.ML201.message.format(id), errorCode = Errors.ML201.code)
        }
        val customer = getOnlyCustomerById(id)
        bookService.deleteBooksByCustomer(customer)

        //não iremo deletar propriaamente dito do banco para não perder historico inclusive e boa pratica por isso customer vai ter um status
        customer.status = CustomerStatus.INATIVO
        updateUser(customer)

        //se eu querer deletar algo do banco
        //customerRepository.deleteById(id)
    }

    fun isExistsEmail(value: String): Boolean {
        //se ele retornar true preciso que retorne false para o validador e assim dar erro
       return !customerRepository.existsByEmail(value)
    }

    fun existsCustomerId(value: Int): Boolean {
       return customerRepository.existsById(value)
    }

}