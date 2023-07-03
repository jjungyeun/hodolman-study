package com.wonjung.hodolstudy1.repository

import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.domain.MemberSession
import org.springframework.data.jpa.repository.JpaRepository

interface MemberSessionRepository: JpaRepository<MemberSession, Long> {
    fun findAllByMember(member: Member): List<MemberSession>
}