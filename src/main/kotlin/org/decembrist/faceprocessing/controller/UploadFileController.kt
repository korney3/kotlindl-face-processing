package org.decembrist.faceprocessing.controller

import org.decembrist.faceprocessing.service.FileStorageImpl
import org.decembrist.faceprocessing.neuralnetwork.AgeGoogleNetModel
import org.decembrist.faceprocessing.neuralnetwork.FANModel
import org.decembrist.faceprocessing.neuralnetwork.GenderGoogleNetModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import javax.xml.bind.DatatypeConverter

@Controller
class UploadFileController(private val fileStorage: FileStorageImpl,
                           private val ageGoogleNetModel: AgeGoogleNetModel,
                           private val fanModel: FANModel,
                           private val genderGoogleNetModel: GenderGoogleNetModel) {

    @GetMapping
    fun index(): String = "uploadform"

    @PostMapping
    fun uploadMultipartFile(@RequestParam("uploadfile") file: MultipartFile, model: Model): String {
        if (file.isEmpty) {
            return "uploadform"
        }
        fileStorage.storeMultipartFile(file, file.originalFilename!!)

        val landmarkFilename = storeLandmarksImage(file)

        model.addImageAttributeToFrontend(file.originalFilename!!, "initialimageurl")
        model.addImageAttributeToFrontend(landmarkFilename, "processedimageurl")

        val age = file.getAgeOfImage()
        val gender = file.getGenderOfImage()

        model.addStringAttributeToFrontend("ageprediction", age)
        model.addStringAttributeToFrontend("genderprediction", gender)

        return "uploadform"
    }


    //todo
    private fun Model.addImageAttributeToFrontend(filename: String, attributeName: String) {
        fileStorage.loadFile(filename).inputStream.use { stream ->
            val imageBase64 = DatatypeConverter.printBase64Binary(stream.readAllBytes())
            addAttribute(
                attributeName,
                "data:;base64,$imageBase64"
            )
        }
    }

    private fun storeLandmarksImage(
        file: MultipartFile,
    ): String {
        val landmarkFilename = fileStorage.getFilenameForLandmarkFile(file.originalFilename!!)
        if (!Files.exists(fileStorage.rootLocation.resolve(landmarkFilename))) {
            val fileResource = fileStorage.loadFile(file.originalFilename!!)
            fanModel.inference(fileResource).use {stream ->
                fileStorage.storeInputStream(stream, landmarkFilename)
            }
        }
        return landmarkFilename
    }

    private fun MultipartFile.getAgeOfImage() : String {
        val fileResource = fileStorage.loadFile(originalFilename!!)
        return ageGoogleNetModel.inference(fileResource)
    }

    private fun MultipartFile.getGenderOfImage() : String {
        val fileResource = fileStorage.loadFile(originalFilename!!)
        return genderGoogleNetModel.inference(fileResource)
    }

    private fun Model.addStringAttributeToFrontend(attributeName: String, attributeValue: String) {
        addAttribute(
            attributeName,
            attributeValue
        )
    }

}