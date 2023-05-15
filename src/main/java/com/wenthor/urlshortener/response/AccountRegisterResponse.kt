package com.wenthor.urlshortener.response

data class AccountRegisterResponse (
    val email: String,
    val role: String?,
    val maxUrls: Int? = 5
)
