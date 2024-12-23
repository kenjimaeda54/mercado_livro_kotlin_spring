package com.mercadolivro.respository.purchase

import com.mercadolivro.resource.customer.CustomerModel
import com.mercadolivro.resource.purchase.PurchaseModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseRepository: JpaRepository<PurchaseModel,Int> {
    fun findByCustomer(customerModel: CustomerModel, pageable: Pageable): Page<PurchaseModel>
}
