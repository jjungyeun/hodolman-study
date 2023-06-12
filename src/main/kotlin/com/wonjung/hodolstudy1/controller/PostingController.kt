package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.log.logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostingController {

    val log = logger()

    @GetMapping("/posts")
    fun get(): String {
        return "Hello World!"
    }

    @PostMapping("/posts")
    fun post(
        @RequestBody createDto: PostingCreateDto
    ): String {
        log.info("title: ${createDto.title}, content: ${createDto.content}")
        return "Hello World!"
    }

}