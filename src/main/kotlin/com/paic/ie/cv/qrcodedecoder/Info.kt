package com.paic.ie.cv.qrcodedecoder

import com.google.zxing.ResultPoint

data class Info(val text: String, val poly: Array<ResultPoint>)

data class Response(var errorCode: Errors, var content: Info?) {
    var errorMsg: String = ""
    init {
        errorMsg = errorCode.message
    }
}