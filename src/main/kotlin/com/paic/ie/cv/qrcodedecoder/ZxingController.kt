package com.paic.ie.cv.qrcodedecoder

import com.google.zxing.*
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.imageio.ImageIO
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer

@RestController
class GreetingController {
    private val logger = LoggerFactory.getLogger(GreetingController::class.java)

    @PostMapping("/decode")
    fun decodeQRCode(@RequestParam(value = "image") name: ByteArray) {
        logger.info("Got image.")
        val image = ImageIO.read(name.inputStream())
        val source = BufferedImageLuminanceSource(image)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val hints: Map<DecodeHintType, Any> = mapOf(DecodeHintType.TRY_HARDER to true,
                DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE))
        val reader = MultiFormatReader()
        val result = reader.decode(bitmap, hints)
        logger.info("Decoding finish.")
        Invoice(result.text)
    }

}