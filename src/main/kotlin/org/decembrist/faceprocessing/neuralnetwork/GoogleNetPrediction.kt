package org.decembrist.faceprocessing.neuralnetwork

import org.decembrist.faceprocessing.neuralnetwork.imageprocessing.ImagePreprocessing
import org.springframework.core.io.Resource
import java.io.File


abstract class GoogleNetPrediction(private val imagePreprocessor: ImagePreprocessing): NeuralNetworkModel {

    fun getPrediction(fileResource: Resource): Int {
        val neuralNetwork = getModel()
        val prediction = neuralNetwork.use {
            val imageFile = File(fileResource.uri)
            val processedImage = imagePreprocessor.googleNetImagePreprocessingForModel(imageFile)
            neuralNetwork.predict(processedImage)

        }
        return prediction
    }
    companion object {
        val MODEL_PATH = "cache/pretrainedModels/models/onnx/googlenet"
    }

}