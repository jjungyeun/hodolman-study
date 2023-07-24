package com.wonjung.hodolstudy1.config

import com.wonjung.hodolstudy1.domain.Member
import com.wonjung.hodolstudy1.repository.MemberRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory::class)
annotation class WithCustomMockUser(
    val username: String = "default",
    val password: String = "1234",
    val roles: Array<String>
)

@Component
class WithCustomMockUserSecurityContextFactory(
    val memberRepository: MemberRepository
): WithSecurityContextFactory<WithCustomMockUser> {
    override fun createSecurityContext(annotation: WithCustomMockUser?): SecurityContext {
        val member = memberRepository.findByEmail(annotation!!.username)
            .orElseGet {
                memberRepository.save(Member(
                    email = annotation.username,
                    name = annotation.username,
                    password = annotation.password
                ))
            }

        val customUserDetails = CustomUserDetails(
            userId = member.id,
            username = member.email,
            password = member.password,
            authorities = annotation.roles.map {
                SimpleGrantedAuthority("ROLE_$it")
            }.toList()
        )

        return SecurityContextHolder.getContext().apply {
            this.authentication = UsernamePasswordAuthenticationToken(
                customUserDetails,
                customUserDetails.password,
                customUserDetails.authorities
            )
        }
    }

}