package com.wonjung.hodolstudy1.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.config.CustomUserDetails
import com.wonjung.hodolstudy1.error.ErrorCode
import com.wonjung.hodolstudy1.log.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class LoginSuccessHandler(
    private val objectMapper: ObjectMapper
) : HttpHandler, AuthenticationSuccessHandler {

    val log = logger()
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication
    ) {
        val userDetails = authentication.principal as CustomUserDetails
        log.info("[인증성공] member: ${userDetails.username} (id: ${userDetails.userId})")
        response?.let { this.writeOkResponse(objectMapper, response, "LOGIN SUCCESS") }
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
        response?.let { this.writeErrorResponse(objectMapper, response, ErrorCode.INVALID_SIGN_IN) }
    }
}