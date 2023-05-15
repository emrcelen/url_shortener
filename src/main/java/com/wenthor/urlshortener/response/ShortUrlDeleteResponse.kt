package com.wenthor.urlshortener.response

data class ShortUrlDeleteResponse(
    val email: String,
    val remaining_urls: Int,
    val url: String,
    val short_url: String,
    val created_date: String,
    val deleted_date: String
)
