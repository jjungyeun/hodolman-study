package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.service.AuthService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
    @Value("\${hodol.jwt-secret-key}") var jwtSecretKeyString: String
) {

    val log = logger()

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginInfo: LoginDto): ResponseEntity<ResponseCookie> {
        val sessionId = authService.signin(loginInfo)
        val jwts = createJwtToken(sessionId)
        val responseCookie = createCookieWithJwt(jwts)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .build()
    }

    private fun createJwtToken(sessionId: String): String {
        val decodedKey = Base64.getDecoder().decode(jwtSecretKeyString)
        val jwtSecretKey = Keys.hmacShaKeyFor(decodedKey)
        return Jwts.builder()
            .setSubject(sessionId)
            .signWith(jwtSecretKey)
            .setIssuedAt(Date())
            .compact()
    }

    private fun createCookieWithJwt(jwts: String): ResponseCookie {
        return ResponseCookie.from("SESSION", jwts)
            .domain("localhost")    // TODO - 환경별로 설정 필요
            .path("/")
            .httpOnly(true)
            .secure(false)
            .maxAge(Duration.ofDays(30))
            .sameSite("Strict")
            .build()
    }
}