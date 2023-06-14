package com.wonjung.hodolstudy1.error

open class CustomException(
    errorCode: ErrorCode,
    msg: String = ""
): RuntimeException(
    if (msg.isBlank()) errorCode.message else "${errorCode.message}: $msg"
)

class PostNotFoundException(
    postId: Long
): CustomException(
    errorCode = ErrorCode.POST_NOT_EXIST,
    msg = "post_id: $postId") {
}