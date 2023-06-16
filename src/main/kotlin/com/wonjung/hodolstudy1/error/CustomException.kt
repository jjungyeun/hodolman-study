package com.wonjung.hodolstudy1.error

open class CustomException(
    val errorCode: ErrorCode,
    msg: String = ""
): RuntimeException(
    if (msg.isBlank()) errorCode.message else "${errorCode.message} - $msg"
)

class PostNotFoundException(
    val postId: Long
): CustomException(
    errorCode = ErrorCode.POST_NOT_EXIST,
    msg = "post_id: $postId") {
}