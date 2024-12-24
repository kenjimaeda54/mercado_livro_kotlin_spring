package com.mercadolivro.security

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.resource.customer.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(private val customerModel: CustomerModel) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = customerModel.role.map {
        SimpleGrantedAuthority(it.description)
    }.toMutableList()

    override fun getPassword(): String = customerModel.password

    override fun getUsername(): String = customerModel.id.toString()

    override fun isCredentialsNonExpired(): Boolean  = true

    override fun isAccountNonLocked(): Boolean  = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isEnabled(): Boolean  = customerModel.status == CustomerStatus.ATIVO
}