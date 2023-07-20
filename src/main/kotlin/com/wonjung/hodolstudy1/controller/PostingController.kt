package com.wonjung.hodolstudy1.controller

import com.wonjung.hodolstudy1.dto.req.PostingCreateDto
import com.wonjung.hodolstudy1.dto.req.PostingEditDto
import com.wonjung.hodolstudy1.dto.req.UserSession
import com.wonjung.hodolstudy1.dto.res.CreateResponseDto
import com.wonjung.hodolstudy1.dto.res.DeleteResponseDto
import com.wonjung.hodolstudy1.dto.res.PostResponseDto
import com.wonjung.hodolstudy1.log.logger
import com.wonjung.hodolstudy1.service.PostingService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.*
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostingController(
    val postingService: PostingService
) {

    val log = logger()

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    fun getPostsWithPaging(
        @PageableDefault(size = 10, sort = ["id"], direction = DESC) pageable: Pageable
    ): ResponseEntity<Page<PostResponseDto>> {
        val responseDtos = postingService.getPostsWithPaging(pageable)
        return ResponseEntity.ok()
            .body(responseDtos)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{postId}")
    fun editPost(@PathVariable postId: Long,
                 @RequestBody editDto: PostingEditDto
    ): ResponseEntity<PostResponseDto> {
        val responseDto = postingService.editPost(postId, editDto)
        return ResponseEntity.ok()
            .body(responseDto)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Long): ResponseEntity<DeleteResponseDto> {
        postingService.deletePost(postId)
        return ResponseEntity.ok()
            .body(DeleteResponseDto(deleted = postId))
    }

}