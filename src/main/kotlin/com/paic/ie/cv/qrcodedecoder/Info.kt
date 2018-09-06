package com.paic.ie.cv.qrcodedecoder

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.zxing.ResultPoint

data class Info(val text: String, val poly: Array<ResultPoint>)

class Response(error: Errors, var content: Info?) {
    @JsonProperty
    private var errorMsg: String = ""
    @JsonProperty
    private var errorCode: Int = 0

    @JsonIgnore
    var error:Errors = error
    set(value) {
        errorCode = value.ordinal
        errorMsg = value.message
    }
}