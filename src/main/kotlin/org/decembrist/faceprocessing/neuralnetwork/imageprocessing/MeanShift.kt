package org.decembrist.faceprocessing.neuralnetwork.imageprocessing

import org.jetbrains.kotlinx.dl.dataset.preprocessor.ImageShape
import org.jetbrains.kotlinx.dl.dataset.preprocessor.Preprocessor
import org.springframework.stereotype.Component

@Component
class MeanShift : Preprocessor {

    override fun apply(data: FloatArray, inputShape: ImageShape): FloatArray {
        val width = inputShape.width!!
        val height = inputShape.height!!
        val channels = inputShape.channels.toInt()
        val channelMeans = FloatArray(3)

        for (i in data.indices) {
            val channel = i % 3
            channelMeans[channel] = channelMeans[channel] + data[i]
        }

        for (channel in 0 until channels) {
            channelMeans[channel] = channelMeans[channel] / (width * height)
        }

        for (i in data.indices) {
            val channel = i % 3
            data[i] = data[i] - channelMeans[channel] + MEAN_SHIFTS[channel]
        }

        return data
    }

    companion object {
        private val MEAN_SHIFTS = listOf(123f, 117f, 104f)
    }
}