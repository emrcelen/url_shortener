package com.wenthor.urlshortener.response.rest.error

data class RestNotvalidMethodResponse(
    val errorDate: String,
    val errorMessage: String,
    val validExample: List<String>,
    val errorCode: Int,
    val detail: String,
){
    constructor(errorDate: String, errorMessage: String,errorCode: Int,detail: String) :
            this(
                errorDate = errorDate,
                errorMessage = errorMessage,
                validExample = listOf("https://example.com","http://www.example.com"),
                errorCode = errorCode,
                detail = detail
            )
}
