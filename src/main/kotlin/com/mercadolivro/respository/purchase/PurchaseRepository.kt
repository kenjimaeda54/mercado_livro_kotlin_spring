package com.mercadolivro.respository.purchase

import com.mercadolivro.resource.purchase.PurchaseModel
import org.springframework.data.repository.CrudRepository

interface PurchaseRepository: CrudRepository<PurchaseModel,Int> {

}
