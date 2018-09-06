package com.paic.ie.cv.qrcodedecoder

import com.google.zxing.*
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.imageio.ImageIO
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.springframework.web.multipart.MultipartFile

@RestController
class GreetingController {
    private val logger = LoggerFactory.getLogger(GreetingController::class.java)

    @PostMapping("/decode")
    fun decodeQRCode(@RequestParam(value = "image") name: MultipartFile): Response {
        logger.info("Got image.")
        val image = ImageIO.read(name.inputStream)
        val source = BufferedImageLuminanceSource(image)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val hints: Map<DecodeHintType, Any> = mapOf(DecodeHintType.TRY_HARDER to true,
                DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE))
        val reader = MultiFormatReader()

        val response = Response(Errors.OK, null)

        val content: Result? = try {
            response.errorCode = Errors.OK
            reader.decode(bitmap, hints)
        }
        catch(e: NotFoundException){
            response.errorCode = Errors.NotFoundError
            null
        }
        catch(e: ChecksumException){
            response.errorCode = Errors.ChecksumError
            null
        }
        catch(e: FormatException){
            response.errorCode = Errors.FormatError
            null
        }

        if (response.errorCode != Errors.OK) {
            logger.error(response.errorMsg)
        } else {
            logger.debug("Decoded message: ${content!!.text}")
            response.content = Info(content!!.text, content!!.resultPoints)
        }
        logger.info("Decoding finish.")
        return response
    }

}