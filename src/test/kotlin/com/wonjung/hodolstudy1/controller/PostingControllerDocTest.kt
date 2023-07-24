package com.wonjung.hodolstudy1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.wonjung.hodolstudy1.config.WithCustomMockUser
import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.domain.Post
import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.repository.PostRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.snippet.Attributes
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(
    uriScheme = "https",
    uriHost = "api.wonjung.com",
    uriPort = 443
)
@ExtendWith(RestDocumentationExtension::class)
class PostingControllerDocTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val postRepository: PostRepository,
    @Autowired val memberRepository: MemberRepository,
    @Autowired val objectMapper: ObjectMapper
) {

    @Test
    @DisplayName("글 단건 조회")
    fun doc_test_1() {
        // given
        val member = Member(
            email = "hello@gmail.com",
            password = "1234",
            name = "hello"
        )
        memberRepository.save(member)

        val post = Post(
            title = "This is title",
            content = "This is content~",
            member = member
        )
        postRepository.save(post)

        // when & then
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/posts/{postId}", post.id)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(post.id))
            .andExpect(jsonPath("$.title").value(post.title))
            .andExpect(jsonPath("$.content").value(post.content))
            .andDo(document("post-select-one",
                pathParameters(
                    parameterWithName("postId").description("게시글 ID")
                ),
                responseFields(
                    fieldWithPath("id").description("게시글 ID"),
                    fieldWithPath("title").description("게시글 제목"),
                    fieldWithPath("content").description("게시글 내용")
                )
            ))
    }


    @Test
    @DisplayName("글 등록")
    @WithCustomMockUser(username = "hello@gmail.com", roles = ["ADMIN"])
    fun posting_save_test() {
        // given
        val title = "제목입니다."
        val content = "내용입니다."
        val requestDto = PostingCreateDto(title = title, content = content)

        // when
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
            .andExpect(status().isOk)
            .andDo(document("post-create",
                requestFields(
                    fieldWithPath("title").description("게시글 제목").attributes(Attributes.key("constraint").value("좋은 제목 부탁해요^^")),
                    fieldWithPath("content").description("게시글 내용").optional()
                ),
                responseFields(
                    fieldWithPath("created").description("생성된 게시글 ID")
                )
            ))

        // then
        Assertions.assertEquals(1, postRepository.count())
        val savedPost = postRepository.findAll()[0]
        Assertions.assertEquals(title, savedPost.title)
        Assertions.assertEquals(content, savedPost.content)
    }
}