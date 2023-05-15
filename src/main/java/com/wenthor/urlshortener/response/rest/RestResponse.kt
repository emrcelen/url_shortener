package com.wenthor.urlshortener.response.rest

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class RestResponse<T> : Serializable {
    var payload: T? = null
        private set
    var responseDate: String? = null
    var isSuccess = false
    fun setPayload(payload: T) {
        this.payload = payload
    }

    constructor(payload: T, isSuccess: Boolean) {
        val sdf = SimpleDateFormat("dd/MM/YYYY HH:mm:ss")
        this.payload = payload
        this.isSuccess = isSuccess
        responseDate = sdf.format(Date())
    }

    constructor(payload: T, responseDate: String?, isSuccess: Boolean) {
        this.payload = payload
        this.responseDate = responseDate
        this.isSuccess = isSuccess
    }

    companion object {
        fun <T> of(t: T): RestResponse<T> {
            return RestResponse(t, true)
        }
        fun <T> error(t: T): RestResponse<T> {
            return RestResponse(t, false)
        }
    }
}