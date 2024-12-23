package com.mercadolivro.controller.purchase

import com.mercadolivro.controller.request.purchase.PostPurchaseRequest
import com.mercadolivro.mapper.PurchaseMapper
import com.mercadolivro.resource.purchase.PurchaseModel
import com.mercadolivro.service.purchase.PurchaseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private const val REQUEST_MAPPING = "purchases"

@RestController
@RequestMapping(REQUEST_MAPPING)
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPurchase(@RequestBody @Valid purchase: PostPurchaseRequest) {
        purchaseService.create(purchaseMapper.toModel(request = purchase))
    }

}