package org.decembrist.faceprocessing.neuralnetwork

import org.decembrist.faceprocessing.neuralnetwork.imageprocessing.FANImageSaving
import org.decembrist.faceprocessing.neuralnetwork.imageprocessing.FAN_IMAGE_SIZE_VISUALIZATION
import org.decembrist.faceprocessing.neuralnetwork.imageprocessing.ImagePreprocessing
import org.jetbrains.kotlinx.dl.api.inference.loaders.ONNXModelHub
import org.jetbrains.kotlinx.dl.api.inference.onnx.ONNXModels
import org.jetbrains.kotlinx.dl.api.inference.onnx.facealignment.Fan2D106FaceAlignmentModel
import org.jetbrains.kotlinx.dl.dataset.preprocessor.ImageShape
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.File

@Component
class FANModel(private val imagePreprocessor: ImagePreprocessing): NeuralNetworkModel {

    override fun inference(fileResource: Resource): ByteArrayInputStream {
        val neuralNetwork = getModel()
        val landmarksInputStream = neuralNetwork.use {
            val imageFile = File(fileResource.uri)
            val landmarks = it.detectLandmarks(imageFile = imageFile)
            val rawImage = imagePreprocessor.fanImagePreprocessingForVisualization(imageFile)
            FANImageSaving.saveLandMarks(rawImage, ImageShape(FAN_IMAGE_SIZE_VISUALIZATION.toLong(), FAN_IMAGE_SIZE_VISUALIZATION.toLong(), 3), landmarks)
        }
        return landmarksInputStream
    }

    override fun getModel(): Fan2D106FaceAlignmentModel {
        val modelHub = ONNXModelHub(cacheDirectory = File("cache/pretrainedModels"))
        return ONNXModels.FaceAlignment.Fan2d106.pretrainedModel(modelHub)
    }


}