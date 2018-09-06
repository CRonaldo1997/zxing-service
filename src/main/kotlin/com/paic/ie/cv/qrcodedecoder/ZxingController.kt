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
        val response = Response(Errors.OK, null)
        val image = try {
            ImageIO.read(name.inputStream)!!
        } catch (e: NullPointerException){
            logger.error(Errors.FileFormatError.message)
            response.error = Errors.FileFormatError
            return response
        }

        logger.info("Got image.")

        val source = BufferedImageLuminanceSource(image)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val hints: Map<DecodeHintType, Any> = mapOf(DecodeHintType.TRY_HARDER to true,
                DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE))
        val reader = MultiFormatReader()



        val content: Result? = try {
            response.error = Errors.OK
            reader.decode(bitmap, hints)
        }
        catch(e: NotFoundException){
            response.error = Errors.NotFoundError
            null
        }
        catch(e: ChecksumException){
            response.error = Errors.ChecksumError
            null
        }
        catch(e: FormatException){
            response.error = Errors.FormatError
            null
        }

        if (response.error != Errors.OK) {
            logger.error(response.error.message)
        } else {
            logger.debug("Decoded message: ${content!!.text}")
            response.content = Info(content!!.text, content!!.resultPoints)
        }
        logger.info("Decoding finish.")
        return response
    }

}