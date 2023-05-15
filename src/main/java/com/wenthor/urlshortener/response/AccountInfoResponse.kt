package com.wenthor.urlshortener.response

import java.math.BigInteger

data class AccountInfoResponse(
    val email: String,
    val role: String,
    val remaining_urls: BigInteger,
)
