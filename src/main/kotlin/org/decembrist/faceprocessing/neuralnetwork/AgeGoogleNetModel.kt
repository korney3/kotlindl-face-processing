package org.decembrist.faceprocessing.neuralnetwork

import org.decembrist.faceprocessing.neuralnetwork.imageprocessing.ImagePreprocessing
import org.jetbrains.kotlinx.dl.api.inference.InferenceModel
import org.jetbrains.kotlinx.dl.api.inference.onnx.OnnxInferenceModel
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class AgeGoogleNetModel(imagePreprocessor: ImagePreprocessing): GoogleNetPrediction(imagePreprocessor) {
    override fun getModel(): InferenceModel {
        return OnnxInferenceModel.load("${MODEL_PATH}/age_googlenet.onnx")
    }

    override fun inference(fileResource: Resource): String {
        val prediction = getPrediction(fileResource)
        return ageList[prediction]
    }
}