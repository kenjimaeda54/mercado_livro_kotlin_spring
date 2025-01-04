package com.mercadolivro.helper

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.ProfileRoles
import com.mercadolivro.resource.book.BookModel
import com.mercadolivro.resource.customer.CustomerModel
import com.mercadolivro.resource.purchase.PurchaseModel
import java.math.BigDecimal
import java.util.*

class MocksModels {
    companion object {

         fun customerBuilder(
            id: Int? = null,
            name: String = "customer name",
            email: String = "$${UUID.randomUUID()}@gmail.com",
            password: String = "password",
        ) = CustomerModel(
            id = id,
            name = name,
            email = email,
            password = password,
            status = CustomerStatus.ATIVO,
            role = setOf(ProfileRoles.CUSTOMER)
        )

        fun purchaseBuilder(
           id: Int? = null,
           price: BigDecimal = BigDecimal.TEN,
           nfe: String? =  Random().nextFloat().toString(),
           customer: CustomerModel = customerBuilder(),
           books: MutableList<BookModel> = mutableListOf()
        ) = PurchaseModel(
            id = id,
            price = price,
            nfe = nfe,
            customer = customer,
            books = books
        )

    }
}