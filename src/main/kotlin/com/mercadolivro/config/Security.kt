package com.mercadolivro.config

import com.mercadolivro.respository.customer.CustomerRepository
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.service.customer.UserCustomDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class Security(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val customerRepository: CustomerRepository,
    private val userCustomDetailsService: UserCustomDetailsService,
    private val jwtUtil: JwtUtil
) {

    private val lisMatchers = arrayOf(
        "/customers"
    )

    @Bean
    fun bcryptEncoder() = BCryptPasswordEncoder()

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(bcryptEncoder())
        authenticationProvider.setUserDetailsService(userCustomDetailsService)
        return authenticationProvider
    }

    @Bean
    fun configureFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { csrf ->
            csrf.disable()
        }.cors { cors ->
            cors.disable()
        }.sessionManagement { session ->
            //nunca vai ter um authenticate vivo na aplicaçao toda
            //precisa sempre autenticar para ter acesso
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.authorizeHttpRequests { authorize ->
            //se nã passar o permitAll preciso criar regras
            //dai o admin poderia ter acesso a outras coisas
            authorize.requestMatchers(HttpMethod.POST, *lisMatchers).permitAll()

            //qualifier outra rota precisa ser autenticado
            //sem isso aqui não ira adiantar criar o filter, pois ira continuar a dar falha
            authorize.anyRequest().authenticated()
        }.addFilter(
            customAuthenticationFilter(
                authenticationManager = authenticationManager(authenticationConfiguration)
            )
        ).addFilter(
            customAuthorizationFilter(
                authenticationManager = authenticationManager(
                    authenticationConfiguration
                )
            )
        )
            .build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun customAuthenticationFilter(authenticationManager: AuthenticationManager) =
        AuthenticationFilter(authenticationManager, customerRepository, jwtUtil)

    @Bean
    fun customAuthorizationFilter(authenticationManager: AuthenticationManager) =
        AuthorizationFilter(authenticationManager, jwtUtil, userCustomDetailsService)
}