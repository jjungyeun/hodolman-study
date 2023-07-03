package com.wonjung.hodolstudy1.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Member(
    email: String,
    password: String,
    name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "member_id")
    val id: Long = 0

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false, name = "member_email")
    var email: String = email
        private set

    @Column(nullable = false, name = "member_password")
    var password: String = password
        private set

    @Column(nullable = false, name = "member_name")
    var name: String = name
        private set

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL])
    val sessions: MutableList<MemberSession> = mutableListOf()

    fun addSession(): String {
        val session = MemberSession(this)
        this.sessions.add(session)
        return session.token
    }

}