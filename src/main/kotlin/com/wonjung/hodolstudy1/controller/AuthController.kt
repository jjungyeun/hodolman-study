package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.error.InvalidSignInException
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.repository.MemberRepository
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    val memberRepository: MemberRepository
) {

    val log = logger()

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginInfo: LoginDto): String {
//        log.info("email: ${loginInfo.email}, pw: ${loginInfo.password}")

        val member = memberRepository.findByEmailAndPassword(loginInfo.email!!, loginInfo.password!!)
            .orElseThrow { InvalidSignInException() }

        return member.name
    }
}