package com.wonjung.hodolstudy1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.dto.req.SignupDto
import com.wonjung.hodolstudy1.repository.MemberRepository
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
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val memberRepository: MemberRepository
){

    @BeforeEach // 각 테스트 메소드가 실행되기 전에 실행되는 메소드
    fun tearDown() {
        memberRepository.deleteAll()
    }

    @Test
    @DisplayName("회원가입 테스트")
    fun signup_test() {
        // given
        val requestDto = SignupDto(email = "hello@google.com", name = "안녕", password = "1234")

        // when
        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        // then
        assertEquals(1, memberRepository.count())
    }

}