package com.wonjung.hodolstudy1.error

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.dto.res.ErrorResponseDto
import com.wonjung.hodolstudy1.log.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

interface HttpHandler {
    fun writeResponse(objectMapper: ObjectMapper, response: HttpServletResponse, errorCode: ErrorCode) {
        response.status = errorCode.status.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        objectMapper.writeValue(
            response.writer, ErrorResponseDto(
                status = errorCode.status.value(),
                errorCode = errorCode.toString(),
                message = errorCode.message
            )
        )
    }
}

@Component
class Http403Handler(
    private val objectMapper: ObjectMapper
) : HttpHandler, AccessDeniedHandler {

    val log = logger()

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        log.error("[인증오류] 403")
        response?.let { this.writeResponse(objectMapper, response, ErrorCode.FORBIDDEN) }
    }
}

@Component
class Http401Handler(
    private val objectMapper: ObjectMapper
) : HttpHandler, AuthenticationEntryPoint {

    val log = logger()

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        log.error("[인증오류] 401")
        response?.let { this.writeResponse(objectMapper, response, ErrorCode.UNAUTHORIZED) }
    }
}

@Component
class LoginFailHandler(
    private val objectMapper: ObjectMapper
) : HttpHandler, AuthenticationFailureHandler {

    val log = logger()
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        log.error("[인증오류] 아이디 혹은 비밀번호가 올바르지 않습니다.")
        response?.let { this.writeResponse(objectMapper, response, ErrorCode.INVALID_SIGN_IN) }
    }

}