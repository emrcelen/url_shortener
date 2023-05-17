package com.wenthor.urlshortener.response

data class PermKeyInfoResponse(
    val key: String,
    val role_name: String,
    val createdDate: String,
    val expirationDate: String,
    val activated: String,
    val account_name: String,
)
