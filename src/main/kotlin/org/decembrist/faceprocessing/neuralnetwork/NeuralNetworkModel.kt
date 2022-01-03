package org.decembrist.faceprocessing.neuralnetwork

import org.jetbrains.kotlinx.dl.api.inference.InferenceModel
import org.springframework.core.io.Resource

interface NeuralNetworkModel {
    fun getModel(): InferenceModel
    fun inference(fileResource: Resource): Any
}