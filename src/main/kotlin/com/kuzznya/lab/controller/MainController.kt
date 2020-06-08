package com.kuzznya.lab.controller

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.State
import com.kuzznya.lab.model.TensionField
import com.kuzznya.lab.service.DataReader
import com.kuzznya.lab.service.Router
import com.kuzznya.lab.view.ResizableCanvas
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import java.io.File

class MainController {
    @FXML
    private lateinit var canvasPane: AnchorPane

    private var canvas: Canvas = ResizableCanvas()

    @FXML
    fun initialize() {
        canvasPane.children.add(canvas)
        canvas.widthProperty().bind(canvasPane.widthProperty())
        canvas.heightProperty().bind(canvasPane.heightProperty())
    }

    private fun drawTension(field: TensionField, points: Set<Point>) {
        val xMin = points.minBy { it.x }?.x!!
        val xMax = points.maxBy { it.x }?.x!!
        val yMin = points.minBy { it.y }?.y!!
        val yMax = points.maxBy { it.y }?.y!!

        val ctx = canvas.graphicsContext2D
        ctx.fill = Color.BLACK
        ctx.fillRect(0.0, 0.0, canvas.width, canvas.height)

        val tensionValues = emptyMap<Point, Double>().toMutableMap()
        for (point in points)
            tensionValues[point] = field.getTension(point.x, point.y)

        val minTension = tensionValues.values.sorted()[(tensionValues.values.size / 10.0 * 2.0).toInt()]
        val maxTension = tensionValues.values.sorted()[(tensionValues.values.size / 10.0 * 8.0).toInt()]

//        for (x in 0..canvas.width.toInt()) {
//            for (y in 0..canvas.height.toInt()) {
//                var tensionValue = field.getTension(
//                    x / canvas.width * (xMax - xMin) + xMin,
//                    y / canvas.height * (yMax - yMin) + yMin
//                )
//                ctx.pixelWriter.setColor(x, y,
//                    Color.hsb(
//                        tensionValue / maxTension * 240.0,
//                        1.0,
//                        1.0
//                    )
//                )
//            }
//        }

        for (point in points) {
//            ctx.pixelWriter.setColor(((point.x - xMin) / (xMax - xMin) * canvas.width).toInt(),
//                ((point.y - yMin) / (yMax - yMin) * canvas.height).toInt(),
//                Color.hsb(tensionValues[point]!! / maxTension * 240.0, 1.0, 1.0 ))
            ctx.fill = Color.hsb(
                (tensionValues[point]!! - minTension) / (maxTension - minTension) * (360.0 - 240.0) + 240.0,
                1.0,
                1.0
            )

            ctx.fillOval(
                (point.x - xMin) / (xMax - xMin) * canvas.width,
                (point.y - yMin) / (yMax - yMin) * canvas.height,
                1.0, 1.0)
        }
    }

    @FXML
    private fun loadData() {
        val file: File = FileChooser().showOpenDialog(Router.primaryStage.owner) ?: return
        val data = DataReader(file.path).loadData()
//        for (point in data.keys)
//            println("${point.x} ${point.y} ${data[point]}")
//        println(data.keys.size)
        println(data.keys.minBy { it.x }?.x)
        println(data.keys.maxBy { it.x }?.x)
        println(data.keys.minBy { it.y }?.y)
        println(data.keys.maxBy { it.y }?.y)

        val field = TensionField(data)
//        for (point in data.keys)
//            println(field.getTension(point.x, point.y))

        drawTension(field, data.keys)
    }

    @FXML
    private fun saveData() {

    }

    @FXML
    private fun exportImage() {

    }
}