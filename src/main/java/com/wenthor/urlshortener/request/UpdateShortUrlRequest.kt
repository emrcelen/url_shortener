package com.wenthor.urlshortener.request

data class UpdateShortUrlRequest(
    val previous_url: String,
    val update_url: String
)
