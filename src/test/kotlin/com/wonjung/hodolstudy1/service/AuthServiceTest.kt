package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.dto.req.SignupDto
import com.wonjung.hodolstudy1.error.DuplicatedEmailException
import com.wonjung.hodolstudy1.error.InvalidSignInException
import com.wonjung.hodolstudy1.error.MemberNotFoundException
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.repository.MemberSessionRepository
import com.wonjung.hodolstudy1.util.AuthUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AuthServiceTest(
    @Autowired val authService: AuthService,
    @Autowired val authUtil: AuthUtil,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val sessionRepository: MemberSessionRepository
) {

    @BeforeEach // 각 테스트 메소드가 실행되기 전에 실행되는 메소드
    fun tearDown() {
        memberRepository.deleteAll()
    }

    @Test
    @DisplayName("회원가입 성공")
    fun signup_test() {
        // given
        val signupDto = SignupDto(email = "hello@google.com", name = "안녕", password = "1234")

        // when
        val createdId = authService.signup(signupDto)

        // then
        assertEquals(1, memberRepository.count())
        val foundMember = memberRepository.findById(createdId)
            .orElseThrow { MemberNotFoundException(createdId) }
        assertEquals(signupDto.email, foundMember.email)
        assertEquals(signupDto.name, foundMember.name)
        assertTrue(authUtil.matchPassword(signupDto.password!!, foundMember.password))
    }

    @Test
    @DisplayName("회원가입 시 이메일 중복 체크")
    fun signup_duplicate_test() {
        // given
        val signupDto1 = SignupDto(email = "hello@google.com", name = "안녕", password = "1234")
        val signupDto2 = SignupDto(email = "hello@google.com", name = "안녕12", password = "12345")

        // when & then
        authService.signup(signupDto1)
        assertThrows(DuplicatedEmailException::class.java) { authService.signup(signupDto2) }
    }

    @Test
    @DisplayName("로그인 성공")
    fun signin_test() {
        // given
        val member = memberRepository.save(
            Member(
                email = "hello@google.com",
                name = "안녕",
                password = authUtil.encodePassword("1234")
            )
        )
        val requestDto = LoginDto(email = member.email, password = "1234")

        // when
        authService.signin(requestDto)

        // then
        assertEquals(1, sessionRepository.count())

    }

    @Test
    @DisplayName("비밀번호가 틀려서 로그인 실패")
    fun signin_fail_test() {
        // given
        val member = memberRepository.save(
            Member(
                email = "hello@google.com",
                name = "안녕",
                password = authUtil.encodePassword("1234")
            )
        )
        val requestDto = LoginDto(email = member.email, password = "12345")

        // when & then
        assertThrows(InvalidSignInException::class.java) { authService.signin(requestDto) }
    }

}