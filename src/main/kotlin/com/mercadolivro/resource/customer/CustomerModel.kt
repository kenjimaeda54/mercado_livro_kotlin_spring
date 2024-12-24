package com.mercadolivro.resource.customer

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.ProfileRoles
import jakarta.persistence.*

private const val TABLE_NAME = "Customer"
private const val COLUMN_ROLE = "role"
private const val JOIN_COLUMN = "customer_id"
private const val CUSTOMER_ROlES = "Customer_Roles"

@Entity
@Table(name = TABLE_NAME)
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
    var password: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus,


    //aqui iremos injetar em outra table por isso o uso do CollectionTable
    //tambem do ElementCollection
    //dai vai ser criado uma tabela com role        customer
    //                                statusRole idDoCustomer
    @Column(name= COLUMN_ROLE)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = CUSTOMER_ROlES, joinColumns = [JoinColumn(name = JOIN_COLUMN)])
    //fetch eager vai sempre buscar
    //tambem nessa tabeal
    @ElementCollection(targetClass = ProfileRoles::class, fetch = FetchType.EAGER)
    var role: Set<ProfileRoles> = setOf()

)
