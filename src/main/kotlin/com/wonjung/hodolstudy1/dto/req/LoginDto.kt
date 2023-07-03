package com.wonjung.hodolstudy1.dto.req

import jakarta.validation.constraints.NotBlank

data class LoginDto(
    @field:NotBlank(message = "이메일을 입력해주세요.")
    val email: String? = "",
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String? = ""
)
