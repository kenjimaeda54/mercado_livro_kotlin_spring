package com.mercadolivro.respository.controllers.customer

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.mercadolivro.controller.request.customer.PostCustomerRequest
import com.mercadolivro.controller.request.customer.PutCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.helper.MocksModels.Companion.customerBuilder
import com.mercadolivro.respository.customer.CustomerRepository
import com.mercadolivro.security.UserCustomDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.Random
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration
@WithMockUser
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private lateinit var mockVck: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() = customerRepository.deleteAll()

    @AfterEach
    fun dowTear() = customerRepository.deleteAll()


    @Nested
    inner class AlUser {

        @Test
        fun `should return list Customer without filter by name`() {
            val customerOne = customerRepository.save(customerBuilder())
            val customerTwo = customerRepository.save(customerBuilder())


            mockVck.perform(get("/customers"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(customerOne.id))
                .andExpect(jsonPath("$[0].name").value(customerOne.name))
                .andExpect(jsonPath("$[0].email").value(customerOne.email))
                .andExpect(jsonPath("$[0].status").value(customerOne.status.name))
                .andExpect(jsonPath("$[1].id").value(customerTwo.id))
                .andExpect(jsonPath("$[1].name").value(customerTwo.name))
                .andExpect(jsonPath("$[1].email").value(customerTwo.email))
                .andExpect(jsonPath("$[1].status").value(customerTwo.status.name))

        }

        @Test
        fun `should return one Customer when filter by name`() {
            //cuidado para filtrar por nome preciso no mock do custoerBuilder
            //salvar com um nome, se nao ele não ira encontrar e retornar tudo
            val customer = customerRepository.save(customerBuilder(name = "Gustavo"))
            customerRepository.save(customerBuilder(name = "Ricardo"))


            mockVck.perform(get("/customers?name=Gu"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(customer.id))
                .andExpect(jsonPath("$[0].name").value(customer.name))
                .andExpect(jsonPath("$[0].email").value(customer.email))
                .andExpect(jsonPath("$[0].status").value(customer.status.name))


        }
    }

    @Nested
    inner class CreateUser {

        @Test
        fun `should create customer if all field are filled`() {
            val postCustomer =
                PostCustomerRequest(name = "Ricardo", password = "password", email = "${Random().nextInt()}@gmail.com")


            mockVck.perform(
                post("/customers").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postCustomer))
            )
                .andExpect(status().isCreated)

            val customerExpected = customerRepository.findAll().toList().first()

            //password não e possivel testar por que esta criptografado
            assertEquals(customerExpected.name, postCustomer.name)
            assertEquals(customerExpected.email, postCustomer.email)
        }

        @Test
        fun `should throws error if something field required  not filled`() {
            val postCustomer =
                PostCustomerRequest(name = "", password = "password", email = "${Random().nextInt()}@gmail.com")

            mockVck.perform(
                post("/customers").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postCustomer))
            )
                .andExpect(status().isUnprocessableEntity)
                .andExpect(jsonPath("$.message").value("Invalid request"))
                .andExpect(jsonPath("$.errorCode").value("ML-001"))
                .andExpect(jsonPath("$.httpStatusCode").value("422"))
        }
    }

    @Nested
    inner class GetById {

        @Test
        fun `should return one Customer if find id`() {
            //cuidado para filtrar por nome preciso no mock do custoerBuilder
            //salvar com um nome, se nao ele não ira encontrar e retornar tudo
            val customer = customerRepository.save(customerBuilder())

            //uando preciso de validação para saber se esta pegando apenas seu recurso
            // posso usar with(user(Classe que valida))
            //auiq nao retorna lista por isso nao posso usar o length
            //se nao vai dar comportamento estranho
            mockVck.perform(get("/customers/${customer.id}").with(user(UserCustomDetails(customer))))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value(customer.name))
                .andExpect(jsonPath("$.email").value(customer.email))
                .andExpect(jsonPath("$.status").value(customer.status.name))

        }

        @Test
        fun `should throws error if id not authorized`() {

            val customer = customerRepository.save(customerBuilder())

            mockVck.perform(get("/customers/0").with(user(UserCustomDetails(customer))))
                .andExpect(status().isForbidden)
        }


        //mmaneira de testar se o admiin tem acesso a todos valores
        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun `should return customer if admin`() {

            val customer = customerRepository.save(customerBuilder())

            mockVck.perform(get("/customers/${customer.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value(customer.name))
                .andExpect(jsonPath("$.email").value(customer.email))
                .andExpect(jsonPath("$.status").value(customer.status.name))
        }
    }

    @Nested
    inner class UpdateUser {

        @Test
        fun `should update customer if id exist`() {
            val putCustomerRequest = PutCustomerRequest(
                name = "Ricardo",
                email = "exammple@gmail.com",
            )
            val customer = customerRepository.save(
                customerBuilder(
                    name = putCustomerRequest.name ?: "",
                    email = putCustomerRequest.email ?: ""
                )
            )


            //uando preciso de validação posso usar with(user(Classe que valida))
            //auiq nao retorna lista por isso nao posso usar o length
            //se nao vai dar comportamento estranho
            mockVck.perform(
                put("/customers/${customer.id}").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(putCustomerRequest))
            )
                .andExpect(status().isNoContent)

            val customerExcepted = customerRepository.findById(customer.id!!)

            assertEquals(customerExcepted.get().name, customer.name)
            assertEquals(customerExcepted.get().email, customer.email)
        }

        @Test
        fun `should throws error if customer id not exist`() {
            val putCustomerRequest = PutCustomerRequest(
                name = "Ricardo",
                email = "exammple@gmail.com",
            )
            customerRepository.save(
                customerBuilder(
                    name = putCustomerRequest.name ?: "",
                    email = putCustomerRequest.email ?: ""
                )
            )

            mockVck.perform(
                put("/customers/0").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(putCustomerRequest))
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.httpStatusCode").value(404))
                .andExpect(jsonPath("$.errorCode").value("ML-201"))
                .andExpect(jsonPath("$.message").value("Customer {${0}} not exist"))

        }

    }

    @Nested
    inner class DeleteUser {

        @Test
        fun `should delete customer`() {

            val customer = customerRepository.save(
                customerBuilder(
                )
            )

            mockVck.perform(
                delete("/customers/${customer.id}")
            )
                .andExpect(status().isNoContent)

            val customerExcepted = customerRepository.findById(customer.id!!)

            assertEquals(customerExcepted.get().status, CustomerStatus.INATIVO)
        }

        @Test
        fun `should throws error if id not exists`() {
            mockVck.perform(
                delete("/customers/0")
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.httpStatusCode").value(404))
                .andExpect(jsonPath("$.errorCode").value("ML-201"))
                .andExpect(jsonPath("$.message").value("Customer {${0}} not exist"))

        }
    }


}