package com.mercadolivro.extension.customer

import com.mercadolivro.controller.request.customer.PostCustomerRequest
import com.mercadolivro.controller.request.customer.PutCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.resource.customer.CustomerModel


fun PostCustomerRequest.toModel() = CustomerModel(
    name = this.name,
    email = this.email,
    status = CustomerStatus.ATIVO,
    password = this.password,
)

fun PutCustomerRequest.toModel(previous: CustomerModel) = CustomerModel(
    id = previous.id,
    name = this.name ?: previous.name,
    email = this.email ?: previous.email,
    status = previous.status,
    password = previous.password
)