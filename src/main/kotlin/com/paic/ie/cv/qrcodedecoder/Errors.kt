package com.paic.ie.cv.qrcodedecoder

enum class Errors(val message: String){
    OK(""),
    NotFoundError("QRCode is not found."),
    ChecksumError("Checksum is wrong."),
    FormatError("Format is wrong."),
    FileFormatError("File is not image.")
}