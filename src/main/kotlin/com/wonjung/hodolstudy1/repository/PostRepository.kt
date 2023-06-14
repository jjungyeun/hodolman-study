package com.wonjung.hodolstudy1.repository

import com.wonjung.hodolstudy1.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
}