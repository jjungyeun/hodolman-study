package com.wonjung.hodolstudy1.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthUtil(
    @Value("\${hodol.jwt-secret-key}") var jwtSecretKeyString: String
) {
    fun createJwtToken(sessionId: String): String {
        val decodedKey = Base64.getDecoder().decode(jwtSecretKeyString)
        val jwtSecretKey = Keys.hmacShaKeyFor(decodedKey)
        return Jwts.builder()
            .setSubject(sessionId)
            .signWith(jwtSecretKey)
            .setIssuedAt(Date())
            .compact()
    }
}