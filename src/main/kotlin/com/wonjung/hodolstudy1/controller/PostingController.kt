package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.log.logger
import jakarta.validation.Valid
import org.springframework.validation.BindingResult
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
        @RequestBody @Valid createDto: PostingCreateDto,
//        br: BindingResult
    ): Map<String, String?> {
        log.info("title: ${createDto.title}, content: ${createDto.content}")

//        if (br.hasErrors()) {
//            val fieldErrors = br.getFieldErrors()
//            val firstFieldError = fieldErrors.get(0)
//
//            val errors: MutableMap<String, String?> = mutableMapOf()
//            errors.put(firstFieldError.field, firstFieldError.defaultMessage)
//            return errors
//        }

        return mapOf()
    }

}