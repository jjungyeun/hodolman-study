package com.wonjung.hodolstudy1.repository

import com.wonjung.hodolstudy1.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmailAndPassword(email: String, password: String): Optional<Member>
    fun findByEmail(email: String): Optional<Member>
}