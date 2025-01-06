package com.mercadolivro.respository.customer

import com.mercadolivro.helper.MocksModels.Companion.customerBuilder
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.*

//cuidado repara a herarqquia do resources
// -- test
//     |
//     | ==> kotlin
//     | ==> resources
@SpringBootTest
@ActiveProfiles("test") //vai rodar e criar um banco de test para nos
//ele encontra o test profile
//porue nosso profile esta application-test
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    //estamos testando uuma interface por isso esta sendo diferente
    @Autowired
    private lateinit var repository: CustomerRepository


    //preciso sempre limpar a base
    @BeforeEach
    fun setup() = repository.deleteAll()

    @Test
    fun `should find by name if containing`() {
        val firstCustomer = repository.save(customerBuilder(name = "Lucas"))
        val secondCustomer = repository.save(customerBuilder(name = "Ricardo"))

        val expectedFirstCustomer = repository.findByNameContaining("Luc")
        val expectedSecondCustomer = repository.findByNameContaining("Rica")

        assertContains(expectedFirstCustomer, firstCustomer)
        assertContains(expectedSecondCustomer, secondCustomer)
    }

    //aqui e separando por suites de testes
    @Nested
    inner class VerifyEmail {

        @Test
        fun `should assert true is exist email`() {
            repository.save(customerBuilder(email = "lucas@examplo.com"))

            val expectedCustomerExist = repository.existsByEmail("lucas@examplo.com")

            assertTrue(expectedCustomerExist)
        }

        @Test
        fun `should assert false is  don't exist email`() {
            repository.save(customerBuilder(email = "lucas@examplo.com"))

            val expectedCustomerExist = repository.existsByEmail("fernando@examplo.com")

            assertFalse(expectedCustomerExist)
        }
    }

    @Nested
    inner class FindByEmail {

        @Test
        fun `should assert equal customer expected if find email`() {
            val customer = repository.save(customerBuilder(email = "lucas@examplo.com"))

            val expectedCustomer = repository.findByEmail("lucas@examplo.com")

            assertNotNull(expectedCustomer)
            assertEquals(expectedCustomer,customer)
        }

        @Test
        fun `should assert null  if not find email`() {
            repository.save(customerBuilder(email = "lucas@examplo.com"))

            val expectedCustomer = repository.findByEmail("fernando@examplo.com")

            assertNull(expectedCustomer)
        }
    }

}