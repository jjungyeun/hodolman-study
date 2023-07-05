package com.wonjung.hodolstudy1.config

import com.wonjung.hodolstudy1.dto.req.UserSession
import com.wonjung.hodolstudy1.error.UnAuthorizedException
import com.wonjung.hodolstudy1.repository.MemberSessionRepository
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.HttpServletRequest
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

@Component
class AuthResolver(
    val sessionRepository: MemberSessionRepository,
    @Value("\${hodol.jwt-secret-key}") var jwtSecretKeyString: String
): HandlerMethodArgumentResolver {

//    @Value("\${jwt.secret-key}")
//    lateinit var jwtSecretKeyString: String

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
        val servletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw UnAuthorizedException()

        val cookie = servletRequest.cookies
            .findLast { it.name == "SESSION" }
            ?: throw UnAuthorizedException()

        val accessToken = cookie.value
        if (accessToken.isNullOrEmpty()) {
            throw UnAuthorizedException()
        }

        try {
            val decodedKey = Base64.decodeBase64(jwtSecretKeyString)
            val claims = Jwts.parserBuilder()
                .setSigningKey(decodedKey)
                .build()
                .parseClaimsJws(accessToken)

            val sessionId = claims.body.subject
            val memberSession = sessionRepository.findByToken(sessionId)
                .orElseThrow { UnAuthorizedException() }
            return UserSession(memberSession.member.id)
        } catch (e: JwtException) {
            throw UnAuthorizedException()
        }
    }
}

