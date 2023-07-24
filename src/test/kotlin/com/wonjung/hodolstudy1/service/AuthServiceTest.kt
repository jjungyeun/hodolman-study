package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.dto.req.SignupDto
import com.wonjung.hodolstudy1.error.DuplicatedEmailException
import com.wonjung.hodolstudy1.error.MemberNotFoundException
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.util.AuthUtil
import org.junit.jupiter.api.AfterEach
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
    @Autowired val memberRepository: MemberRepository
) {

    @AfterEach // 각 테스트 메소드가 실행된 후에 실행되는 메소드
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

}