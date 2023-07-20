package com.wonjung.hodolstudy1.error

import com.wonjung.hodolstudy1.dto.res.ErrorResponseDto
import com.wonjung.hodolstudy1.log.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerExceptionHandler {

    val log = logger()

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        val errorCode = ErrorCode.FIELD_VALIDATION_ERROR
        val errorResponseDto = ErrorResponseDto(
            status = errorCode.status.value(),
            errorCode = errorCode.toString(),
            message = errorCode.message
        ).apply {
            ex.fieldErrors.forEach { fieldError ->
                this.addValidation(ValidationError(fieldError.field, fieldError.defaultMessage))
            }
        }

        return ResponseEntity.badRequest()
            .body(errorResponseDto)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<ErrorResponseDto> {
        val errorCode = ErrorCode.FORBIDDEN
        return ResponseEntity
            .status(errorCode.status.value())
            .body(ErrorResponseDto(
                    status = errorCode.status.value(),
                    errorCode = errorCode.toString(),
                    message = ex.message
                )
            )
    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(ex.errorCode.status)
            .body(ErrorResponseDto(
                    status = ex.errorCode.status.value(),
                    errorCode = ex.errorCode.toString(),
                    message = ex.message
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponseDto> {
        log.error(ex.stackTraceToString())
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponseDto(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    errorCode = HttpStatus.INTERNAL_SERVER_ERROR.name,
                    message = ex.message
                )
            )
    }
}