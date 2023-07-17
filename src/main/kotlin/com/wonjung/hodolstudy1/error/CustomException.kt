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
    msg = "post_id: $postId")

class MemberNotFoundException(
    memberId: Long? = null
): CustomException(
    errorCode = ErrorCode.MEMBER_NOT_EXIST,
    msg = if (memberId == null) "" else "member_id: $memberId")

class UnAuthorizedException(): CustomException(
    errorCode = ErrorCode.UNAUTHORIZED
)

class ForbiddenException(): CustomException(
    errorCode = ErrorCode.FORBIDDEN
)

class InvalidSignInException: CustomException(
    errorCode = ErrorCode.INVALID_SIGN_IN
)

class DuplicatedEmailException: CustomException(
    errorCode = ErrorCode.EMAIL_DUPLICATED
)