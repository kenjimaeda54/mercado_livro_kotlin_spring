package com.mercadolivro.events.listeners.purchase

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.service.purchase.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PurchaseListener(
    private val purchaseService: PurchaseService
) {

    @Async
    @EventListener
    fun purchaseEvent(purchaseEvent: PurchaseEvent){
        val nfe = UUID.randomUUID().toString()
        val purchaseModel = purchaseEvent.purchaseModel.copy(
            nfe = nfe
        )
        purchaseService.update(purchaseModel)

    }

}