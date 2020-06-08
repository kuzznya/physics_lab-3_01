package com.kuzznya.lab.controller

import com.kuzznya.lab.service.DataReader
import com.kuzznya.lab.service.Router
import com.kuzznya.lab.view.ResizableCanvas
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import java.io.File

class MainController {
    @FXML
    private lateinit var canvasPane: AnchorPane

    private var canvas: Canvas = ResizableCanvas()

    @FXML
    fun initialize() {
        canvas.widthProperty().bind(canvasPane.widthProperty())
        canvas.heightProperty().bind(canvasPane.heightProperty())
    }

    @FXML
    private fun loadData() {
        val file: File = FileChooser().showOpenDialog(Router.primaryStage.owner) ?: return
        val data = DataReader(file.path).loadData()
        for (point in data.keys)
            println("${point.x} ${point.y} ${data[point]}")
        println(data.keys.size)
    }

    @FXML
    private fun saveData() {

    }

    @FXML
    private fun exportImage() {

    }
}