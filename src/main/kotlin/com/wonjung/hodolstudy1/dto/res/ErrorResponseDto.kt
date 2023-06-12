package com.wonjung.hodolstudy1.dto.res

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.wonjung.hodolstudy1.error.ValidationError

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ErrorResponseDto(
    val status: Int,
    val errorCode: String,
    val message: String?,
    val validation: MutableList<ValidationError> = mutableListOf()
) {
    fun addValidation(ve: ValidationError) {
        this.validation.add(ve)
    }
}
