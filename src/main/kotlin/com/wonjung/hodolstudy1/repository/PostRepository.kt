package com.wonjung.hodolstudy1.repository

import com.wonjung.hodolstudy1.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
    override fun findAll(pageable: Pageable): Page<Post>
}