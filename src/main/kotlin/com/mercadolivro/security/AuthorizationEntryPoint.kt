package com.mercadolivro.security

import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.response.error.ErrorResponse
import com.mercadolivro.enums.Errors
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthorizationEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = "application/json"
        response.status = HttpStatus.FORBIDDEN.value()
        val errorResponse = ErrorResponse(HttpStatus.FORBIDDEN.value(), errorCode = Errors.ML000.code, message = Errors.ML000.message, fieldErrors = null)
        response.outputStream.print(jacksonObjectMapper().writeValueAsString(errorResponse))
    }

}