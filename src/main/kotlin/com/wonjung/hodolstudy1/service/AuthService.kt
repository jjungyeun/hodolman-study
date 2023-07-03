package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.error.InvalidSignInException
import com.wonjung.hodolstudy1.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    val memberRepository: MemberRepository
) {

    @Transactional
    fun signin(loginDto: LoginDto): String {
        val member = memberRepository.findByEmailAndPassword(loginDto.email!!, loginDto.password!!)
            .orElseThrow { InvalidSignInException() }
        return member.addSession()
    }
}