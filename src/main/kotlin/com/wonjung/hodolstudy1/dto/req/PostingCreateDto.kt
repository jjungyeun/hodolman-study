package com.wonjung.hodolstudy1.dto.req

import jakarta.validation.constraints.NotBlank

data class PostingCreateDto(
    @field:NotBlank
    val title: String? = "",
    @field:NotBlank
    val content: String? = ""
)