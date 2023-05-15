package com.wenthor.urlshortener.response


data class LogShortUrlUserInfoResponse(
    val url: String,
    val previous_url: String,
    val update_url: String,
    val url_updated_date: String
)
