package com.wonjung.hodolstudy1.service

import com.wonjung.hodolstudy1.domain.Post
import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostingService(
    val postRepository: PostRepository
) {
    val log = logger()

    fun write(createDto: PostingCreateDto): Long {
        val post = Post(
            title = createDto.title!!,
            content = createDto.content!!
        )
        postRepository.save(post)
        return post.id
    }
}