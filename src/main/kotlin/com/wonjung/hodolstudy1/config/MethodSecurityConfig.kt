package com.wonjung.hodolstudy1.config

import com.wonjung.hodolstudy1.error.ForbiddenException
import com.wonjung.hodolstudy1.error.PostNotFoundException
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.repository.PostRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Configuration
@EnableMethodSecurity()
class MethodSecurityConfig(
    val postRepository: PostRepository
) {

    @Bean
    fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
        return DefaultMethodSecurityExpressionHandler().apply {
            this.setPermissionEvaluator(HodologPermissionEvaluator(postRepository))
        }
    }
}

@Component
class HodologPermissionEvaluator(
    val postRepository: PostRepository
): PermissionEvaluator {

    val log = logger()

    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        return false
    }

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        val userDetails = authentication?.principal as CustomUserDetails
        val post = postRepository.findById(targetId as Long)
            .orElseThrow { PostNotFoundException(targetId) }
        if (post.member.id != userDetails.userId) {
            log.error("[인가실패] 직접 작성한 글이 아닙니다. (post id: ${targetId}, member id: ${userDetails.userId}")
            return false
        }
        return true
    }

}

