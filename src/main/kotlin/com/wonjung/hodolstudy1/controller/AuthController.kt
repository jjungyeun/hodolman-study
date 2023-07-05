package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.dto.res.SessionResponseDto
import com.wonjung.hodolstudy1.error.InvalidSignInException
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService
) {

    val log = logger()

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginInfo: LoginDto): ResponseEntity<ResponseCookie> {
        val accessToken = authService.signin(loginInfo)
        val responseCookie = ResponseCookie.from("SESSION", accessToken)
            .domain("localhost")    // TODO - 환경별로 설정 필요
            .path("/")
            .httpOnly(true)
            .secure(false)
            .maxAge(Duration.ofDays(30))
            .sameSite("Strict")
            .build()
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .build()
    }
}