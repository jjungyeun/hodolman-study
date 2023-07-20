package com.wonjung.hodolstudy1.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.dto.res.ErrorResponseDto
import com.wonjung.hodolstudy1.error.ErrorCode
import com.wonjung.hodolstudy1.log.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

interface HttpHandler {
    fun writeErrorResponse(objectMapper: ObjectMapper, response: HttpServletResponse, errorCode: ErrorCode) {
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

    fun writeOkResponse(objectMapper: ObjectMapper, response: HttpServletResponse, message: String) {
        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        objectMapper.writeValue(
            response.writer, SuccessResponseDto(
                status = HttpStatus.OK.value(),
                message = message
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
        response?.let { this.writeErrorResponse(objectMapper, response, ErrorCode.FORBIDDEN) }
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
        response?.let { this.writeErrorResponse(objectMapper, response, ErrorCode.UNAUTHORIZED) }
    }
}

data class SuccessResponseDto(
    val status: Int,
    val message: String? = ""
)