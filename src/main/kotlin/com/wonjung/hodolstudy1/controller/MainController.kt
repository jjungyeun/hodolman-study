package com.wonjung.hodolstudy1.controller

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
}