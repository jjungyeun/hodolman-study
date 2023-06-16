package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.domain.Post
import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.dto.res.PostResponseDto
import com.wonjung.hodolstudy1.error.PostNotFoundException
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PostingService(
    val postRepository: PostRepository
) {
    val log = logger()

    fun write(createDto: PostingCreateDto): Long {
        log.info("Write post (title: ${createDto.title}, content: ${createDto.content}).")
        val post = Post(
            title = createDto.title!!,
            content = createDto.content!!
        )
        postRepository.save(post)
        return post.id
    }

    fun getOne(postId: Long): PostResponseDto {
        log.info("Get post (id: $postId).")
        return postRepository.findById(postId)
            .orElseThrow { PostNotFoundException(postId) }
            .run {
                PostResponseDto(
                    id = this.id,
                    title = this.title,
                    content = this.content
                )
            }
    }

    fun getAll(): List<PostResponseDto> {
        log.info("Get all posts.")
        return postRepository.findAll()
            .map { post ->
                PostResponseDto(
                    id = post.id,
                    title = post.title,
                    content = post.content
                )
            }
    }

    fun getPostsWithPaging(pageable: Pageable): Page<PostResponseDto> {
        return postRepository.getList(pageable)
            .map { post ->
                PostResponseDto(
                    id = post.id,
                    title = post.title,
                    content = post.content
                )
            }
    }
}