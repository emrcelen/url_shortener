package com.wenthor.urlshortener.response

import java.time.LocalDateTime

data class AccountLoginTokenResponse(
    val email: String,
    val token: String,
    val tokenIssuedTime: String,
    val tokenExpirationTime: String
)
