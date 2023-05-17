package com.wenthor.urlshortener.response

data class PermKeyResponse(
    val key: String,
    val role_name: String,
    val createdDate: String,
    val expirationDate: String,
)
