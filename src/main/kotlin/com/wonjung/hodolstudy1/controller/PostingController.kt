package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.dto.res.CreateResponseDto
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.service.PostingService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostingController(
    val postingService: PostingService
) {

    val log = logger()

    @GetMapping("/posts")
    fun get(): String {
        return "Hello World!"
    }

    @PostMapping("/posts")
    fun post(
        @RequestBody @Valid createDto: PostingCreateDto
    ): ResponseEntity<CreateResponseDto> {
        val createdId = postingService.write(createDto)
        return ResponseEntity.ok()
                .body(CreateResponseDto(created = createdId))
    }

}