package com.wonjung.hodolstudy1.config

import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.repository.MemberRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DataInit(
    val memberRepository: MemberRepository
) {
    @PostConstruct
    fun init() {
        memberRepository.save(Member(email = "wjyddd@naver.com", password = "1234", name = "원정연"))
        memberRepository.save(Member(email = "hello@naver.com", password = "12345", name = "HELLO"))
        memberRepository.save(Member(email = "test@google.com", password = "123", name = "테스트"))
    }

}