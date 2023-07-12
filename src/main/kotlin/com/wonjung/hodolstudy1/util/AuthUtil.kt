package com.wonjung.hodolstudy1.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthUtil {

    private val encoder = SCryptPasswordEncoder(16, 8, 1, 32, 64)

    fun encodePassword(originalPassword: String): String {
        return encoder.encode(originalPassword)
    }

    fun matchPassword(signinPassword: String, encryptedPassword: String): Boolean {
        return encoder.matches(signinPassword, encryptedPassword)
    }
}