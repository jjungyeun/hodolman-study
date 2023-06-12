package com.wonjung.hodolstudy1.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    FIELD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "잘못된 값이 포함된 요청입니다.")
}