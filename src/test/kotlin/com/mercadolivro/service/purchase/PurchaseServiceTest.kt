package com.mercadolivro.service.purchase

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helper.MocksModels.Companion.customerBuilder
import com.mercadolivro.helper.MocksModels.Companion.purchaseBuilder
import com.mercadolivro.resource.purchase.PurchaseModel
import com.mercadolivro.respository.purchase.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {

    @MockK
    private lateinit var purchaseRepositoryMock: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisherMock: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseServiceMock: PurchaseService

    //tenho que passar o evento ue vou publicar
    //nmo caso e o PurchaseEvent
    //slot e bom para caputar o parametro de um metodo
    //posso inclusive usar priimitivo slot<Int>
    //uma classe nesse exemplo mockei o PurchaseEvent
    private var eventSlot = slot<PurchaseEvent>()


    @Test
    fun `should save purchase`() {
        val purchase = purchaseBuilder()

        every { purchaseRepositoryMock.save(purchase) } returns purchase
        every { applicationEventPublisherMock.publishEvent(capture(eventSlot)) } just runs

        purchaseServiceMock.create(purchase)

        verify(exactly = 1) {
            purchaseRepositoryMock.save(purchase)
        }
        verify(exactly = 1) {
            applicationEventPublisherMock.publishEvent(capture(eventSlot))

        }
        //quero saber quando o publishEvent for disparado vai ser o valor caputurado
        //igual ao model
        //se olhar o serviço real eu passo o model e o srouce que o this
        //entao com slot consigo caputarr o valor que esta pasasndo no parametro
        assertEquals(purchase, eventSlot.captured.purchaseModel)
    }

    @Test
    fun `should update purchase`() {
        val purchase = purchaseBuilder()

        every { purchaseRepositoryMock.save(purchase) } returns purchase

        purchaseServiceMock.update(purchase)

        verify(exactly = 1) {
            purchaseRepositoryMock.save(purchase)
        }
    }

    @Test
    fun `should get  purchase by customer id`() {
        val customer = customerBuilder()
        val purchase = purchaseBuilder()
        val pageable = PageRequest.of(0, 10)
        val pageCustomer: Page<PurchaseModel> = PageImpl(listOf(purchase), pageable, 10)

        every { purchaseRepositoryMock.findByCustomer(customer, pageable) } returns pageCustomer

        purchaseServiceMock.getPurchaseByCustomerId(customer, pageable)

        verify(exactly = 1) {
            purchaseRepositoryMock.findByCustomer(customer, pageable)
        }
    }

}