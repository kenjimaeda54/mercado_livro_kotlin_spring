package com.mercadolivro.resource.customer

import com.mercadolivro.enums.CustomerStatus
import jakarta.persistence.*

@Entity
@Table(name = "Customer")
data class CustomerModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    //se for diferente daa coluna do banco de dados so passar dentro do construtor
    //Column(novo nome)
    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus
)
