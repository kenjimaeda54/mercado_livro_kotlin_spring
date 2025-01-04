package com.mercadolivro.events.listener.purchase

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.events.listeners.purchase.PurchaseListener
import com.mercadolivro.helper.MocksModels
import com.mercadolivro.helper.MocksModels.Companion.purchaseBuilder
import com.mercadolivro.service.purchase.PurchaseService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class PurchaseListenerTest {

    @MockK
    private lateinit var purchaseService: PurchaseService

    @InjectMockKs
    private lateinit var purchaseListener: PurchaseListener

    @Test
    fun `should save purchase`() {
        val purchase = purchaseBuilder(nfe = null)
        val nfe = UUID.randomUUID()
        //a partir de agora o uuid esta mocado
        //isso e uma maneira de mocar clases de terceiro
        //porqque repara na classe real eu uso o nfe ggerado
        //a partir do UUI,se nao o teste iria qquebrar
        //pois a cada momento seriia um novo nfe que nao iriia
        //concidir com o nosso
        mockkStatic(UUID::class)
        val purchaseExpected = purchase.copy(nfe = nfe.toString())

        every { purchaseService.update(purchaseExpected) } just runs
        every { UUID.randomUUID() } returns nfe

        purchaseListener.purchaseEvent(PurchaseEvent(this, purchase))

        verify(exactly = 1) {
            purchaseService.update(purchaseExpected)
        }

    }

}