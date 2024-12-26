package com.mercadolivro.security

import com.mercadolivro.exception.AuthenticationException
import com.mercadolivro.service.customer.UserCustomDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


class AuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userCustomDetailsService: UserCustomDetailsService
): BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
         val authorization = request.getHeader("Authorization")
        //start with vai olhar todo o conjunto se possuir Bearer ira retornar
        //pois o Bearer tem um espaço apos o authorization
         if(authorization != null && authorization.startsWith("Bearer ")) {
             val auth = getAuth(authorization.split(" ")[1])
             SecurityContextHolder.getContext().authentication = auth
         }
         chain.doFilter(request,response)
    }

    private fun  getAuth(token: String): UsernamePasswordAuthenticationToken {
        if(jwtUtil.isInvalidToken(token)) {
            throw AuthenticationException(message = "Token Invalid", httpCode = 999)
        }
        val subject = jwtUtil.getId(token)
        val customer = userCustomDetailsService.loadUserByUsername(subject)
        return UsernamePasswordAuthenticationToken(subject,null,customer.authorities)
    }


}