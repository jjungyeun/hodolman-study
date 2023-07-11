package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.dto.req.SignupDto
import com.wonjung.hodolstudy1.error.DuplicatedEmailException
import com.wonjung.hodolstudy1.error.InvalidSignInException
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.util.AuthUtil
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    val memberRepository: MemberRepository,
    val authUtil: AuthUtil
) {

    @Transactional
    fun signin(loginDto: LoginDto): String {
        val member = memberRepository.findByEmail(loginDto.email!!)
            .orElseThrow { InvalidSignInException() }

        if (!authUtil.matchPassword(loginDto.password!!, member.password))
            throw InvalidSignInException()

        return member.addSession()
    }

    @Transactional
    fun signup(signupDto: SignupDto): Long {
        memberRepository.findByEmail(signupDto.email!!)
            .ifPresent { throw DuplicatedEmailException() }

        val newMember = Member(
            email = signupDto.email,
            name = signupDto.name!!,
            password = authUtil.encodePassword(signupDto.password!!)
        )
        memberRepository.save(newMember)
        return newMember.id
    }
}