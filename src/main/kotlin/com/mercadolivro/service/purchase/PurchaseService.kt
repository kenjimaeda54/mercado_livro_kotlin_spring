package com.mercadolivro.service.purchase

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.resource.purchase.PurchaseModel
import com.mercadolivro.respository.purchase.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.print.PrintService

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
}
