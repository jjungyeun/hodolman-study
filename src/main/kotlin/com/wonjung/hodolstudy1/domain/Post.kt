package com.wonjung.hodolstudy1.domain

import jakarta.persistence.*

@Entity
class Post(
    title: String,
    content: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "post_id")
    val id: Long = 0

    @Column(nullable = false, name = "post_title")
    var title: String = title
        private set

    @Column(nullable = false, name = "post_content")
    @Lob
    var content: String = content
        private set

    fun editTitle(title: String) {
        this.title = title
    }

    fun editContent(content: String) {
        this.content = content
    }
}