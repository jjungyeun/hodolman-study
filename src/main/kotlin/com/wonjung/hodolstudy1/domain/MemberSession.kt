package com.wonjung.hodolstudy1.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
class MemberSession(
    member: Member
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "member_session_id")
    val id: Long = 0

    @Column(nullable = false, name = "member_session_token")
    val token: String = UUID.randomUUID().toString()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member = member
}