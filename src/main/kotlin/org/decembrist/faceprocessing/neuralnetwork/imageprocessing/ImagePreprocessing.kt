package org.decembrist.faceprocessing.neuralnetwork.imageprocessing


import org.jetbrains.kotlinx.dl.dataset.image.ColorOrder
import org.jetbrains.kotlinx.dl.dataset.image.ImageConverter
import org.jetbrains.kotlinx.dl.dataset.preprocessor.*
import org.jetbrains.kotlinx.dl.dataset.preprocessor.image.Cropping
import org.jetbrains.kotlinx.dl.dataset.preprocessor.image.crop
import org.jetbrains.kotlinx.dl.dataset.preprocessor.image.resize
import org.springframework.stereotype.Service
import java.io.File
import kotlin.math.abs

@Service
class ImagePreprocessing(private val meanShift: Preprocessor) {

    fun fanImagePreprocessingForVisualization(
        imageFile: File
    ): FloatArray {
        val initialImageShape: ImageShape = getImageShape(imageFile)
        val centralCropping: Cropping = getCentralCroppingPreprocessor(initialImageShape)

        val preprocessing: Preprocessing = preprocess {
            load {
                pathToData = imageFile
                imageShape = initialImageShape
                colorMode = ColorOrder.BGR
            }
            transformImage {
                crop {
                    top = centralCropping.top
                    bottom = centralCropping.bottom
                    left = centralCropping.left
                    right = centralCropping.right
                }
                resize {
                    outputWidth = FAN_IMAGE_SIZE_VISUALIZATION
                    outputHeight = FAN_IMAGE_SIZE_VISUALIZATION
                }

            }
            transformTensor {
                rescale {
                    scalingCoefficient = 255f
                }
            }
        }

        return preprocessing().first

    }

    fun googleNetImagePreprocessingForModel(
        imageFile: File
    ): FloatArray {
        val initialImageShape = getImageShape(imageFile)
        val centralCropping = getCentralCroppingPreprocessor(initialImageShape)

        val preprocessing: Preprocessing = preprocess {
            load {
                pathToData = imageFile
                imageShape = initialImageShape
                colorMode = ColorOrder.BGR
            }
            transformImage {
                crop {
                    top = centralCropping.top
                    bottom = centralCropping.bottom
                    left = centralCropping.left
                    right = centralCropping.right
                }
                resize {
                    outputWidth = GOOGLENET_IMAGE_SIZE
                    outputHeight = GOOGLENET_IMAGE_SIZE
                }

            }
            transformTensor {
                meanShift()
            }
        }

        return preprocessing().first

    }

    private fun getImageShape(imageFile: File): ImageShape {
        val image = imageFile.inputStream().use { inputStream ->
            ImageConverter.toBufferedImage(inputStream)
        }
        val height = image.height.toLong()
        val width = image.width.toLong()
        return ImageShape(width, height, 3)
    }

    private fun getCentralCroppingPreprocessor(initialImageShape: ImageShape): Cropping = with(initialImageShape) {
        val cropOneSidePixels = ((height!! - width!!) / 2).toInt()
        val cropping = if (cropOneSidePixels > 0) {
            Cropping(top = abs(cropOneSidePixels), bottom = abs(cropOneSidePixels))
        } else {
            Cropping(left = abs(cropOneSidePixels), right = abs(cropOneSidePixels))
        }
        return cropping
    }

    private fun TensorPreprocessing.meanShift() {
        addOperation(meanShift)
    }

}