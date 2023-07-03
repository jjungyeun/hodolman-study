package com.wonjung.hodolstudy1.config

import com.wonjung.hodolstudy1.error.UnAuthorizedException
import com.wonjung.hodolstudy1.log.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor: HandlerInterceptor{

    val log = logger()

    // preHandle(): 컨트롤러 이전에 실행됨
    //              응답 true면 핸들러 어댑테 호출, false면 종료
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val accessToken: String? = request.getParameter("accessToken")
        if (!accessToken.isNullOrEmpty()){
            request.setAttribute("userName", accessToken) // Controller에서 @RequestAttribute로 받을 수 있음
            return true
        }
        throw UnAuthorizedException()
    }
}