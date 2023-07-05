package com.wonjung.hodolstudy1.config

import com.wonjung.hodolstudy1.dto.req.UserSession
import com.wonjung.hodolstudy1.error.UnAuthorizedException
import com.wonjung.hodolstudy1.repository.MemberSessionRepository
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthResolver(
    private val sessionRepository: MemberSessionRepository
): HandlerMethodArgumentResolver {

    // 파라미터에 UserSession이 들어간 모든 API에 대해서 실행됨
    // 서비스 규모가 커지면 커질수록 Interceptor 패턴을 등록하기 복잡해지기 때문에 이 방식도 괜찮음
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == UserSession::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val accessToken: String? = webRequest.getHeader("Authorization")
        if (accessToken.isNullOrEmpty()){
            throw UnAuthorizedException()
        }

        val memberSession = sessionRepository.findByToken(accessToken)
            .orElseThrow { UnAuthorizedException() }

        return UserSession(memberSession.member.id)
    }
}