package com.wenthor.urlshortener.response

data class PremiumShortUrlResponse(
    val url: String,
    val shortUrl: String,
    val createdDate: String,
    val updatedDate: String,
    val expirationDate: String,
    val visitor: Int
)
