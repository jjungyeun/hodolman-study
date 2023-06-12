package com.wonjung.hodolstudy1.error

data class ValidationError(
    val field: String,
    val message: String?
)
