package com.wenthor.todoapp.response

data class RestErrorResponse(
    val errorDate: String?,
    val errorMessage: String?,
    val errorCode: Int?,
    val detail: String?,
)