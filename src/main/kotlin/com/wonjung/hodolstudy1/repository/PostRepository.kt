package com.wonjung.hodolstudy1.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wonjung.hodolstudy1.domain.Post
import com.wonjung.hodolstudy1.domain.QPost.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils

interface PostRepository: JpaRepository<Post, Long>, PostRepositoryCustom {
    override fun findAll(pageable: Pageable): Page<Post>
}

interface PostRepositoryCustom {
    fun getList(pageable: Pageable): Page<Post>
}

class PostRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): PostRepositoryCustom {
    override fun getList(pageable: Pageable): Page<Post> {
        val posts = queryFactory.selectFrom(post)
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .orderBy(getOrder(pageable))
            .fetch()

        val countQuery = queryFactory.select(post.count())
            .from(post)

        return PageableExecutionUtils.getPage(posts, pageable) {countQuery.fetchOne()!!}
    }

    private fun getOrder(pageable: Pageable): OrderSpecifier<Long>? {
        pageable.sort.forEach { sort ->
            if (sort.property == "id")
                return postOrderBy(sort.direction)
        }
        return null
    }

    private fun postOrderBy(direction: Sort.Direction): OrderSpecifier<Long>? {
        return when(direction) {
            Sort.Direction.ASC -> post.id.asc()
            Sort.Direction.DESC -> post.id.desc()
        }
    }
}