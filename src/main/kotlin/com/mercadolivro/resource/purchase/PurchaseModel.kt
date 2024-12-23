package com.mercadolivro.resource.purchase

import com.mercadolivro.resource.book.BookModel
import com.mercadolivro.resource.customer.CustomerModel
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "Purchase")
data class PurchaseModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column
    val price: BigDecimal,

    @Column
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column
    val nfe: String? = null,

    //apenas um purchase pode estar relacionado ao customer
    //porem o customer pode ter varios purchase
    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: CustomerModel?,

    //uma compra pode estar realizado a vario livros
    //varios livros podem estar em varias compras
    @ManyToMany
    @JoinTable(
        name = "Purchase_Book",
        joinColumns = [JoinColumn(name = "purchase_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")]
    )
    val books: MutableList<BookModel>

)
