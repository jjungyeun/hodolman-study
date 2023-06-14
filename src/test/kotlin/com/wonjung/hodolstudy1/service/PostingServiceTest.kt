package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.domain.Post
import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.repository.PostRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PostingServiceTest(
    @Autowired val postingService: PostingService,
    @Autowired val postRepository: PostRepository
) {

    @BeforeEach // 각 테스트 메소드가 실행되기 전에 실행되는 메소드
    fun tearDown() {
        postRepository.deleteAll()
    }

    @Test
    @DisplayName("게시글을 저장한다.")
    fun write_post() {
        // given
        val createDto = PostingCreateDto(title = "제목", content = "내용임")

        // when
        val createdId = postingService.write(createDto)

        // then
        val savedPost = postRepository.findAll()[0]
        assertEquals(savedPost.id, createdId)
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

        // when
        val response = postingService.getOne(post.id)

        // then
        assertEquals(post.id, response.id)
        assertEquals(post.title, response.title)
        assertEquals(post.content, response.content)

    }
}