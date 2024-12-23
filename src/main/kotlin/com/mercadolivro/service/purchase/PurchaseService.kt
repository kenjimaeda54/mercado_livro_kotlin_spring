package com.mercadolivro.service.purchase

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.resource.customer.CustomerModel
import com.mercadolivro.resource.purchase.PurchaseModel
import com.mercadolivro.respository.purchase.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun create(purchase: PurchaseModel) {
        purchaseRepository.save(purchase)
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel = purchase))
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }

    fun getPurchaseByCustomerId(customer: CustomerModel,pageable: Pageable): Page<PurchaseModel> = purchaseRepository.findByCustomer(customer,pageable)

}
