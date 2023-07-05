package com.wonjung.hodolstudy1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.dto.req.LoginDto
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.repository.MemberSessionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val sessionRepository: MemberSessionRepository,
){

    @BeforeEach // 각 테스트 메소드가 실행되기 전에 실행되는 메소드
    fun tearDown() {
        memberRepository.deleteAll()
        sessionRepository.deleteAll()
    }

    @Test
    @DisplayName("로그인 성공 시 세션 생성")
    fun login_test() {
        // given
        val member = Member(email = "wjyddd@naver.com", password = "1234", name = "원정연")
        memberRepository.save(member)

        // when & then
        val requestDto = LoginDto(email = member.email, password = member.password)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())

        assertEquals(1, sessionRepository.findAllByMember(member).size)

    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속할 수 있다")
    fun authorized_test() {
        // given
        val member = Member(email = "wjyddd@naver.com", password = "1234", name = "원정연")
        val token = member.addSession()
        memberRepository.save(member)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/posts/foo")
                .header("Authorization", token)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다")
    fun unauthorized_test() {
        // given
        val member = Member(email = "wjyddd@naver.com", password = "1234", name = "원정연")
        val token = member.addSession()
        memberRepository.save(member)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/posts/foo")
                .header("Authorization", token+"hello")
        )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
            .andDo(MockMvcResultHandlers.print())
    }

}