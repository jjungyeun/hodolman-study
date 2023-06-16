package com.wonjung.hodolstudy1.error

import com.wonjung.hodolstudy1.dto.res.ErrorResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerExceptionHandler {

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
}