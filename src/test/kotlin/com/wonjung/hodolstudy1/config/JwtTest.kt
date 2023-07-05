package com.wonjung.hodolstudy1.config

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.Base64

class JwtTest {
    @Test
    @DisplayName("JWT Secret Key 생성")
    fun create_secret_key() {
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        val keyString = Base64.getEncoder().encodeToString(key.encoded)
        println("CREATED: $keyString")

    }
}