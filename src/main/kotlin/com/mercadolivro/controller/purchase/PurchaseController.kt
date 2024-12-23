package com.mercadolivro.controller.purchase

import com.mercadolivro.controller.request.purchase.PostPurchaseRequest
import com.mercadolivro.controller.response.purchase.PurchaseResponse
import com.mercadolivro.extension.purchase.toResponse
import com.mercadolivro.mapper.PurchaseMapper
import com.mercadolivro.resource.purchase.PurchaseModel
import com.mercadolivro.service.customer.CustomerService
import com.mercadolivro.service.purchase.PurchaseService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    private val purchaseMapper: PurchaseMapper,
    private val customerService: CustomerService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPurchase(@RequestBody @Valid purchase: PostPurchaseRequest) {
        purchaseService.create(purchaseMapper.toModel(request = purchase))
    }

    @GetMapping("/{id}")
    fun getPurchaseByCustomerId(@PathVariable id: Int,@PageableDefault(size = 10, page = 0) pageable: Pageable): Page<PurchaseResponse> {
        val customer = customerService.getOnlyCustomerById(id)
        return purchaseService.getPurchaseByCustomerId(customer,pageable).map { it.toResponse() }
    }

}