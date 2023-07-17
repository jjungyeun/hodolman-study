package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.config.CustomUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/")
    fun main(): String {
        return "메인 페이지입니다."
    }

    @GetMapping("/login-page")
    fun loginPage(): String {
        return "로그인 페이지입니다."
    }

    @GetMapping("/member")
    fun member(@AuthenticationPrincipal userDetails: CustomUserDetails): String {
        val userId = userDetails.userId
        return "사용자(id: $userId) 페이지입니다."
    }

    @GetMapping("/admin")
    fun admin(): String {
        return "관리자 페이지입니다😊"
    }


}