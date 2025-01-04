package com.mercadolivro.service.customer

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.helper.MocksModels.Companion.customerBuilder
import com.mercadolivro.respository.customer.CustomerRepository
import com.mercadolivro.service.book.BookService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.SpyK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    //todas depedencias que o CustomerService precisa
    //vai ser inserido pelo Mocck acima
    //ficar de olho o injectMockks é do mockk
    @InjectMockKs
    @SpyK
    lateinit var customerServiceMock: CustomerService


    @Test
    fun `should return all customers`() {
        val fakeCustomer = listOf(customerBuilder(), customerBuilder())

        every { customerRepository.findAll() } returns fakeCustomer

        val customers = customerServiceMock.getAllUsers(null)

        assertEquals(fakeCustomer, customers)
        verify(exactly = 1) {
            customerRepository.findAll()
        }
        verify(exactly = 0) {
            customerRepository.findByNameContaining(any())
        }

    }

    @Test
    fun `should return all customers with name specific`() {
        val name = Random().nextInt().toString()
        val fakeCustomer = listOf(customerBuilder(), customerBuilder())

        every { customerRepository.findByNameContaining(name) } returns fakeCustomer

        val customers = customerServiceMock.getAllUsers(name)

        assertEquals(fakeCustomer, customers)

        verify(exactly = 1) {
            customerRepository.findByNameContaining(name)
        }
        verify(exactly = 0) {
            customerRepository.findAll()
        }

    }

    @Test
    fun `should create customer with password encrypted`() {
        val initialPassword = Random().nextInt().toString()
        val fakePassword = Random().nextInt().toString()
        val fakeCustomer = customerBuilder(password = initialPassword)
        val customerEncrypted = fakeCustomer.copy(
            password = fakePassword
        )

        //estamos testando service tudo que não for serviice preciso mocar
        //ou sea customerRepositoryy preciso mocar com  every
        every { customerRepository.save(customerEncrypted) } returns customerEncrypted
        every { bCrypt.encode(initialPassword) } returns fakePassword

        //repara ue o service createUser utiliza o model que vem
        //com password inicial
        //no caso e o faakeCustomer
        //se não implementar da forma correta vai dar um erro esquisito
        //eu estava passando o novoCustomer que o customerEncrypeted
        //dai acusava  erro de falta que estava faltando  mocar o bcrypt.Encode
        //na realidade nao estava implementando de forma correta o metodo
        //createUser
        customerServiceMock.createUser(fakeCustomer)

        verify(exactly = 1) {
            customerRepository.save(customerEncrypted)
        }

        verify(exactly = 1) {
            bCrypt.encode(initialPassword)
        }
    }

    @Test
    fun `should return only customer conform id`() {
        val fakeCustomer = customerBuilder()
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerServiceMock.getOnlyCustomerById(id)

        assertEquals(customer, fakeCustomer)
    }

    @Test
    fun `should return throws error when not found customer`() {
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> {
            customerServiceMock.getOnlyCustomerById(id)
        }

        assertEquals("Customer {$id} not exit", error.message)
        assertEquals("ML-201", error.errorCode)
    }

    @Test
    fun `should update customer when exit customer with id`() {
        val id = Random().nextInt()
        val fakeCustomer = customerBuilder(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        customerServiceMock.updateUser(customer = fakeCustomer)

        verify(exactly = 1) {
            customerRepository.save(fakeCustomer)
        }

    }

    @Test
    fun `should cannot update user if don't exist customer id`() {
        val fakeCustomer = customerBuilder(id = null)

        customerServiceMock.updateUser(customer = fakeCustomer)

        verify(exactly = 0) {
            customerRepository.save(fakeCustomer)
        }

    }

    @Test
    fun `should trows error when don't exit customer id`() {
        val id = Random().nextInt()
        val fakeCustomer = customerBuilder(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows<NotFoundException> {
            customerServiceMock.updateUser(customer = fakeCustomer)
        }

        assertEquals("Customer {$id} not exit",error.message)
        assertEquals("ML-201",error.errorCode)
        verify(exactly = 0) {
            customerRepository.save(fakeCustomer)
        }

    }

    @Test
    fun `should delete user when exit customer id`() {
        val id = Random().nextInt()
        val fakeCustomer = customerBuilder(id = id)
        val expectedCustomer  = fakeCustomer.copy(
            status = CustomerStatus.INATIVO
        )

        every { customerRepository.existsById(id) } returns true
        every { bookService.deleteBooksByCustomer(fakeCustomer) } just  runs

        //eu tenho no servico a implementacao de outro service
        //se reparar no deleteUser eu tenho o getOnlyCustomerId
        //isso implementação do proprio serivço real por isso preicso
        //da anotaçÃO SPKY
        every { customerServiceMock.getOnlyCustomerById(id) } returns fakeCustomer
        every { customerServiceMock.updateUser(expectedCustomer) } just runs

        customerServiceMock.deleteUser(id)

        verify(exactly = 1) {
            bookService.deleteBooksByCustomer(fakeCustomer)
        }
        verify(exactly = 1) {
           customerServiceMock.updateUser(fakeCustomer)
        }
    }

    @Test
    fun `should  throws error if when delete user don't exist`() {
        val id = Random().nextInt()
        val fakeCustomer = customerBuilder(id = id)
        val expectedCustomer  = fakeCustomer.copy(
            status = CustomerStatus.INATIVO
        )

        every { customerRepository.existsById(id) } returns false
        every { bookService.deleteBooksByCustomer(fakeCustomer) } just  runs

        every { customerServiceMock.getOnlyCustomerById(id) } returns fakeCustomer
        every { customerServiceMock.updateUser(expectedCustomer) } just runs

        val error = assertThrows<NotFoundException> {
            customerServiceMock.deleteUser(id)
        }

        assertEquals( "Customer {$id} not exit",error.message)
        assertEquals( "ML-201",error.errorCode)

    }

    @Test
    fun `should assert true email is available`() {
        val value = "${Random().nextInt()}@gmail.com"


        every { customerRepository.existsByEmail(value) } returns false

        val isAvailableEmail = customerServiceMock.isExistsEmail(value)

        assertTrue(isAvailableEmail)
    }

    @Test
    fun `should assert false email is unavailable`() {
        val value = "${Random().nextInt()}@gmail.com"


        every { customerRepository.existsByEmail(value) } returns true

        val isAvailableEmail = customerServiceMock.isExistsEmail(value)

        assertFalse(isAvailableEmail)
    }

    @Test
    fun `should assert false if customer id don't exists`() {
        val value = Random().nextInt()

        every { customerRepository.existsById(value) } returns false

        val haveCustomer = customerServiceMock.existsCustomerId(value)

        assertFalse(haveCustomer)
    }

    @Test
    fun `should assert true if customer id exists`() {
        val value = Random().nextInt()

        every { customerRepository.existsById(value) } returns true

        val haveCustomer = customerServiceMock.existsCustomerId(value)

        assertTrue(haveCustomer)
    }

}

