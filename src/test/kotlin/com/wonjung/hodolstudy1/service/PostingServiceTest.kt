package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.config.CustomUserDetails
import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.domain.Post
import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.dto.req.PostingEditDto
import com.wonjung.hodolstudy1.error.PostNotFoundException
import com.wonjung.hodolstudy1.repository.MemberRepository
import com.wonjung.hodolstudy1.repository.PostRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest
class PostingServiceTest(
    @Autowired val postingService: PostingService,
    @Autowired val postRepository: PostRepository,
    @Autowired val memberRepository: MemberRepository
) {

    @AfterEach // 각 테스트 메소드가 실행된 후에 실행되는 메소드
    fun tearDown() {
        postRepository.deleteAll()
        memberRepository.deleteAll()
    }

    @Test
    @DisplayName("게시글을 저장한다.")
    fun write_post() {
        // given
        val member = Member(
            email = "hello@gmail.com",
            password = "1234",
            name = "hello"
        )
        memberRepository.save(member)
        val createDto = PostingCreateDto(title = "제목", content = "내용임")

        // when
        val createdId = postingService.write(member.id, createDto)

        // then
        val savedPost = postRepository.findAll()[0]
        assertEquals(savedPost.id, createdId)
    }

    @Test
    @DisplayName("게시글을 하나 조회한다.")
    fun get_one_post() {
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

        // when
        val response = postingService.getOne(post.id)

        // then
        assertEquals(post.id, response.id)
        assertEquals(post.title, response.title)
        assertEquals(post.content, response.content)

    }

    @Test
    @DisplayName("게시글을 모두 조회한다.")
    fun get_all_posts() {
        // given
        val member = Member(
            email = "hello@gmail.com",
            password = "1234",
            name = "hello"
        )
        memberRepository.save(member)
        val post1 = Post(
            title = "제목",
            content = "내용",
            member = member
        )
        postRepository.save(post1)

        val post2 = Post(
            title = "제목2",
            content = "내용2",
            member = member
        )
        postRepository.save(post2)

        // when
        val response = postingService.getAll()

        // then
        assertEquals(post1.id, response[0].id)
        assertEquals(post1.title, response[0].title)
        assertEquals(post1.content, response[0].content)
        assertEquals(post2.id, response[1].id)
        assertEquals(post2.title, response[1].title)
        assertEquals(post2.content, response[1].content)

    }

    @Test
    @DisplayName("게시글 페이지를 조회한다.")
    fun get_posts_with_paging() {
        // given
        val member = Member(
            email = "hello@gmail.com",
            password = "1234",
            name = "hello"
        )
        memberRepository.save(member)
        for (i in 1..30) {
            val post = Post(
                title = "제목 $i",
                content = "내용 $i",
                member = member
            )
            postRepository.save(post)
        }

        // when
        val response = postingService.getPostsWithPaging(PageRequest.of(0, 3))

        // then
        assertEquals(3, response.content.size)

    }

    @Test
    @DisplayName("게시글 제목을 수정한다.")
    fun edit_post() {
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

        val editDto = PostingEditDto(title = "제목 수정됨")

        // when
        postingService.editPost(post.id, editDto)

        // then
        val editedPost = postRepository.findById(post.id)
            .orElseThrow { PostNotFoundException(post.id) }

        assertEquals(editDto.title, editedPost.title)
        assertEquals(post.content, editedPost.content)

    }

    @Test
    @DisplayName("게시글을 삭제한다.")
    fun delete_post() {
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

        // when
        postingService.deletePost(post.id)

        // then
        assertEquals(0, postRepository.count())
    }

    @Test
    @DisplayName("게시글을 하나 조회하는데 실패한다.")
    fun get_one_post_and_no_post() {
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
        assertThrows(PostNotFoundException::class.java) {postingService.getOne(post.id+1)}

    }

    @Test
    @DisplayName("게시글을 삭제하는 데 실패한다.")
    fun delete_post_and_no_post() {
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
        assertThrows(PostNotFoundException::class.java) {postingService.deletePost(post.id+1)}
    }
}