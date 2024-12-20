package com.mercadolivro.exception

import com.mercadolivro.controller.response.error.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    fun handleException(exception: NotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            httpStatusCode = HttpStatus.NOT_FOUND.value(),
            errorCode = exception.errorCode,
            message = exception.message,
            fieldErrors = null
        )
        return ResponseEntity(error,HttpStatus.NOT_FOUND)
    }

}