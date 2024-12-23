package com.mercadolivro.extension.customer

import com.mercadolivro.controller.response.customer.CustomerResponse
import com.mercadolivro.resource.customer.CustomerModel


fun CustomerModel.toResponse()  = CustomerResponse(
    id = this.id,
    name = this.name,
    email = this.email,
    status = this.status
)