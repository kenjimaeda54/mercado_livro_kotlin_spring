package com.mercadolivro.resource.book

import com.mercadolivro.enums.BooksStatus
import com.mercadolivro.resource.customer.CustomerModel
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "Book")
data class BookModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal, //ideal para trabalhar valor monetario

    @ManyToOne //pode ter varios books para um usuario
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel?
) {

    //quero impedir que o book estiver cancelado ou deletado que mudee o status
    //por isso coloquei fora do contrutor padrão
    //set é muito proximo do didSet do Swiftui
    //https://github.com/kenjimaeda54/concepts-swift
    //eu consiigo recuperaar antes do status ser definido o novo vvalor e o antigo
    //novo e o value nesse caso, antigo sera o field
    @Enumerated(EnumType.STRING)
    @Column
    var status: BooksStatus? = null
        set(value) {
            //se o valor antigo do book ja e deletado ou cancelado não posso alterar
            if (field == BooksStatus.DELETADO || field == BooksStatus.CANCELADO)
                throw Exception("Não é permitido alerar book com o status $field")
            field = value
        }

    //apos isto preciiso recriar o construtor padrão
    constructor(
        id: Int? = null, name: String, price: BigDecimal, customer: CustomerModel?,
        status: BooksStatus? = null
    ) : this(id, name, price, customer) {
        this.status = status
    }

}
