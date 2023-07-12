package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.*
import com.wonjung.hodolstudy1.dto.res.CreateResponseDto
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService
) {

    val log = logger()

    @PostMapping("/login")
    fun login(): String {
        return "로그인 페이지"
    }

    @PostMapping("/signup")
    fun signup(@RequestBody signupDto: SignupDto): ResponseEntity<CreateResponseDto> {
        val memberId = authService.signup(signupDto)
        return ResponseEntity.ok()
            .body(CreateResponseDto(created = memberId))
    }
}