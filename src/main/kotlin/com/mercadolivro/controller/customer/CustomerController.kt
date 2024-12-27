package com.mercadolivro.controller.customer

import com.mercadolivro.annotation.customer.UserCanOnlyAccessYourResource
import com.mercadolivro.controller.request.customer.PostCustomerRequest
import com.mercadolivro.controller.request.customer.PutCustomerRequest
import com.mercadolivro.controller.response.customer.CustomerResponse
import com.mercadolivro.extension.customer.toModel
import com.mercadolivro.extension.customer.toResponse
import com.mercadolivro.service.customer.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

private const val REQUEST_MAPPING = "customers"

@RestController
@RequestMapping(REQUEST_MAPPING)
class CustomerController(
    private val customerService: CustomerService  //spring ja resolve a questão de injeçãd dependencia no mobile usa hilt
) {

    @GetMapping
    fun getAllUsers(@RequestParam name: String?): List<CustomerResponse> = customerService.getAllUsers(name).map {
        it.toResponse()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody @Valid customer: PostCustomerRequest) = customerService.createUser(customer.toModel())

    //precisa ser mesmo nome do pathVariable ou seja id com id
    @GetMapping("/{id}")
    @UserCanOnlyAccessYourResource
    fun getOnlyUser(@PathVariable id: Int): CustomerResponse = customerService.getOnlyCustomerById(id).toResponse()

    //idela e a camada de request nao interferir direto no model
    //por isso estou recebendo um putCustomerRequest
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateUser(@PathVariable id: Int, @RequestBody putCustomerRequest: PutCustomerRequest) {
        val customer = customerService.getOnlyCustomerById(id)
        customerService.updateUser(putCustomerRequest.toModel(customer))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Int) = customerService.deleteUser(id)

}