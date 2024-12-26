package com.mercadolivro.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.request.customer.LoginCustomerRequest
import com.mercadolivro.exception.AuthenticationException
import com.mercadolivro.respository.customer.CustomerRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


//precisa implemetnaro repostiory não posso implementaara o serviice aui
//se nao vai dar erro de reciclo com a caamaaada service
//unica camandaa que pode chamar o service e a camada de controller
//restante tem que chamar a repository
class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            //request.inputStream vai passar valor sem mapear
            ///jackson faz a conversao
            val attemptLogin = jacksonObjectMapper().readValue(request.inputStream, LoginCustomerRequest::class.java)
            val id = customerRepository.findByEmail(attemptLogin.email)?.id
            //estou usando o id para não ficar pasando dado sensivel
            val authToken = UsernamePasswordAuthenticationToken(id, attemptLogin.password)
            return authenticationManager.authenticate(authToken)
        } catch (exception: AuthenticationException) {
            throw AuthenticationException(message = "Failed attempt login", httpCode = HttpStatus.BAD_REQUEST.value())
        }

    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
       val id = (authResult.principal as UserCustomDetails).username.toInt()
       val token  = jwtUtil.generateToken(id)


       response.addHeader("Authorization","Bearer $token")
    }
}