package com.mercadolivro.exception

import com.mercadolivro.controller.response.error.ErrorResponse
import com.mercadolivro.controller.response.error.FieldErrors
import com.mercadolivro.enums.Errors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            httpStatusCode = HttpStatus.NOT_FOUND.value(),
            errorCode = exception.errorCode,
            message = exception.message,
            fieldErrors = null
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(
        exception: BadRequestException,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            httpStatusCode = HttpStatus.BAD_REQUEST.value(),
            errorCode = exception.code,
            message = exception.message,
            fieldErrors = null
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleExceptionGenericBadRequest(
        exception: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            httpStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            errorCode = Errors.ML001.code,
            message = Errors.ML001.message,
            exception.bindingResult.fieldErrors.map {
                FieldErrors(
                    message = it.defaultMessage ?: "",
                    field = it.field
                )
            }
        )
        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleExceptionNotReadableException(
        exception: HttpMessageNotReadableException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            httpStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            errorCode = Errors.ML002.code,
            message = Errors.ML002.message,
            null
        )
        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        exception: AccessDeniedException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            httpStatusCode = HttpStatus.FORBIDDEN.value(),
            errorCode = Errors.ML000.code,
            message = Errors.ML000.message,
            fieldErrors = null
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }
}