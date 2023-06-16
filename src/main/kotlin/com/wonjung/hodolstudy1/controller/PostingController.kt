package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.dto.res.CreateResponseDto
import com.wonjung.hodolstudy1.dto.res.PostResponseDto
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.service.PostingService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostingController(
    val postingService: PostingService
) {

    val log = logger()

    @PostMapping
    fun post(
        @RequestBody @Valid createDto: PostingCreateDto
    ): ResponseEntity<CreateResponseDto> {
        val createdId = postingService.write(createDto)
        return ResponseEntity.ok()
                .body(CreateResponseDto(created = createdId))
    }

    @GetMapping("/{postId}")
    fun getOne(
        @PathVariable postId: Long
    ): ResponseEntity<PostResponseDto> {
        val postResponseDto = postingService.getOne(postId)
        return ResponseEntity.ok()
            .body(postResponseDto)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<PostResponseDto>> {
        val responseDtos = postingService.getAll()
        return ResponseEntity.ok()
            .body(responseDtos)
    }

}