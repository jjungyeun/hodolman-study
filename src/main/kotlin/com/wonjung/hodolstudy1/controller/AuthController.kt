package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.dto.res.SessionResponseDto
import com.wonjung.hodolstudy1.error.InvalidSignInException
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService
) {

    val log = logger()

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginInfo: LoginDto): SessionResponseDto {
        val accessToken = authService.signin(loginInfo)
        return SessionResponseDto(accessToken)
    }
}