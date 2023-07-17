package com.wonjung.hodolstudy1.error

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    FIELD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "잘못된 값이 포함된 요청입니다."),

    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),

    POST_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    MEMBER_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    INVALID_SIGN_IN(HttpStatus.BAD_REQUEST, "아이디/비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
}