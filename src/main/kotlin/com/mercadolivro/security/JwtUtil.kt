package com.mercadolivro.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import com.mercadolivro.exception.AuthenticationException


@Component
class JwtUtil {

    //pegando do application
    @Value("\${jwt.secret}")
    private val secretKey: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    fun generateToken(id: Int): String? {
        return expiration?.let {
            getSignKey()?.let {
                //subject e ouqe vai iidentificar o sistema no caso id
                Jwts.builder().claims().subject(id.toString()).expiration(Date(System.currentTimeMillis() + expiration))
                    .and().signWith(getSignKey(), Jwts.SIG.HS256).compact()
            }
        }
    }

    private fun getSignKey(): SecretKey? {
        secretKey?.let {
            val kybBytes = secretKey.toByteArray(StandardCharsets.UTF_8)
            return SecretKeySpec(kybBytes,"HmacSHA256")
        }
        return null
    }

    fun isInvalidToken(token: String): Boolean {
        val claims = getClaims(token)
        return claims.subject == null || claims.expiration == null || Date().after(claims.expiration)
    }

    private fun getClaims(token: String): Claims {
        try {
           return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).payload

        }catch (exception: AuthenticationException) {
            throw AuthenticationException(message = "Invalid Token", httpCode = 99999)
        }
    }

    fun getId(token: String): String {
       return getClaims(token).subject
    }


}