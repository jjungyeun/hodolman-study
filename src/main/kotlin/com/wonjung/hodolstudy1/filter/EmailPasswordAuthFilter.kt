package com.wonjung.hodolstudy1.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

class EmailPasswordAuthFilter(
    private val objectMapper: ObjectMapper
)
    : AbstractAuthenticationProcessingFilter("/auth/login") {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val emailPassword = objectMapper.readValue(request?.inputStream, EmailPassword::class.java)

        val token = UsernamePasswordAuthenticationToken.unauthenticated(
            emailPassword?.email,
            emailPassword?.password
        )
        token.details = this.authenticationDetailsSource.buildDetails(request)
        return this.authenticationManager.authenticate(token)
    }
}

data class EmailPassword(
    val email: String?,
    val password: String?
)