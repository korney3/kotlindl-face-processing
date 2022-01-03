package org.decembrist.faceprocessing.neuralnetwork.imageprocessing

import org.jetbrains.kotlinx.dl.api.inference.facealignment.Landmark
import org.jetbrains.kotlinx.dl.dataset.preprocessor.ImageShape
import org.jetbrains.kotlinx.dl.visualization.swing.LandMarksJPanel
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import javax.swing.JFrame

//todo
object FANImageSaving {

    fun saveLandMarks(dst: FloatArray, imageShape: ImageShape, landmarks: List<Landmark>): ByteArrayInputStream {
        val frame = JFrame("Landmarks")
        frame.contentPane.add(LandMarksJPanel(dst, imageShape, landmarks))
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isResizable = false

        val content = frame.contentPane
        val img = BufferedImage(content.width, content.height, BufferedImage.TYPE_INT_RGB)
        val g2d = img.createGraphics()
        content.printAll(g2d)
        val byteArrayStream = ByteArrayOutputStream()
        ImageIO.write(img, "png", byteArrayStream)

        return ByteArrayInputStream(byteArrayStream.toByteArray())
    }

}