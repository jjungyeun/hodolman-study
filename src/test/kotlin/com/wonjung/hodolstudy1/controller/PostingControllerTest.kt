package com.wonjung.hodolstudy1.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest // MockMvc 빈을 주입해준다.
class PostingControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    fun test() {
        // when
        mockMvc.perform(get("/posts"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hello World!"))
            .andDo(print())
    }

    @Test
    @DisplayName("/posts 요청 시 빈 객체를 출력한다.")
    fun posting_test() {
        // when & then
        mockMvc.perform(
            post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("{}"))
            .andDo(print())
    }

    @Test
    @DisplayName("/posts 요청 시 title, content 값은 필수다.")
    fun posting_test_2() {
        // when & then
        mockMvc.perform(
            post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"title\": \"  \", \"content\": \"내용입니다.\"}")
                .content("{}")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.error_code").value("FIELD_VALIDATION_ERROR"))
            .andExpect(jsonPath("$.validation").isArray)
            .andDo(print())
    }


}