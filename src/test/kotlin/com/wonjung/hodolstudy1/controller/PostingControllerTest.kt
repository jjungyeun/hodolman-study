package com.wonjung.hodolstudy1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.domain.Post
import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.repository.PostRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc // MockMvc 빈을 주입해준다.
@SpringBootTest
class PostingControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val postRepository: PostRepository,
    @Autowired val objectMapper: ObjectMapper
) {

    @BeforeEach // 각 테스트 메소드가 실행되기 전에 실행되는 메소드
    fun tearDown() {
        postRepository.deleteAll()
    }

    @Test
    @DisplayName("GET /posts 요청 시 Hello World를 출력한다.")
    fun get_test() {
        // when
        mockMvc.perform(get("/posts"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hello World!"))
            .andDo(print())
    }

    @Test
    @DisplayName("POST /posts 요청 시 title, content 값은 필수다.")
    fun posting_validation_test() {
        // given
        val content = "내용입니다."
        val requestDto = PostingCreateDto(content = content)

        // when & then
        mockMvc.perform(
            post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.error_code").value("FIELD_VALIDATION_ERROR"))
            .andExpect(jsonPath("$.validation").isArray)
            .andDo(print())
    }

    @Test
    @DisplayName("POST /posts 요청 시 게시글을 DB에 저장한다.")
    fun posting_save_test() {
        // given
        val title = "제목입니다."
        val content = "내용입니다."
        val requestDto = PostingCreateDto(title = title, content = content)

        // when
        mockMvc.perform(
            post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(status().isOk)
            .andDo(print())

        // then
        assertEquals(1, postRepository.count())
        val savedPost = postRepository.findAll()[0]
        assertEquals(title, savedPost.title)
        assertEquals(content, savedPost.content)
    }

    @Test
    @DisplayName("게시글을 하나 조회한다.")
    fun get_one_post() {
        // given
        val post = Post(
            title = "제목",
            content = "내용"
        )
        postRepository.save(post)

        // when & then
        mockMvc.perform(
            get("/posts/{postId}", post.id))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(post.id))
            .andExpect(jsonPath("$.title").value(post.title))
            .andExpect(jsonPath("$.content").value(post.content))
            .andDo(print())

    }


}