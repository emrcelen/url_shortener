package com.wenthor.urlshortener.response


data class UserShortUrlResponse(
    val url: String,
    val shortUrl: String,
    val createdDate: String,
    val expirationDate: String,
    val visitor: Int
)