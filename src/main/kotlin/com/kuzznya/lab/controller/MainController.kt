package com.kuzznya.lab.controller

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.TensionField
import com.kuzznya.lab.service.ComputationDisplay
import com.kuzznya.lab.service.DataReader
import com.kuzznya.lab.service.Router
import com.kuzznya.lab.view.ResizableCanvas
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Supplier

class MainController {
    @FXML
    private lateinit var canvasPane: AnchorPane
    @FXML
    private lateinit var loadDataButton: Button

    private lateinit var computationTask: CompletableFuture<Unit>

    private lateinit var pool: ExecutorService

    @FXML
    fun initialize() {}


    private fun createComputationDisplay(data: Map<Point, Double>, canvas: Canvas): ComputationDisplay {
        val field = TensionField(data)
        val tensionValues = emptyList<Double>().toMutableList()
        data.keys.forEach { tensionValues.add(field.getTension(it.x, it.y)) }
        val maxTension = tensionValues.sorted()[(tensionValues.size * 0.9).toInt()]

        val xMin = data.keys.minBy { it.x }?.x!!
        val xMax = data.keys.maxBy { it.x }?.x!!
        val yMin = data.keys.minBy { it.y }?.y!!
        val yMax = data.keys.maxBy { it.y }?.y!!

        return ComputationDisplay(field, canvas, 0.0, maxTension, Point(xMin, yMin), Point(xMax, yMax))
    }

    @FXML
    private fun loadData() {
        val file: File = FileChooser().showOpenDialog(Router.primaryStage.owner) ?: return
        Router.primaryStage.title = file.name
        Router.primaryStage.isResizable = false
        loadDataButton.isDisable = true

        val canvas = ResizableCanvas()
        canvasPane.children.clear()
        canvasPane.children.add(canvas)
        canvas.widthProperty().bind(canvasPane.widthProperty())
        canvas.heightProperty().bind(canvasPane.heightProperty())

        if (this::pool.isInitialized)
            pool.shutdown()
        pool = Executors.newFixedThreadPool(4)

        computationTask = CompletableFuture
            .supplyAsync( Supplier { DataReader(file.path).loadData() }, pool)
            .thenApply { data -> createComputationDisplay(data, canvas) }
            .thenCombine (
                CompletableFuture
                    .supplyAsync { Platform.runLater { loadDataButton.isDisable = false } }
            ) { it, _ -> it.compute() }
            .thenRun { Platform.runLater { Router.primaryStage.isResizable = true } }
            .handle { _, u -> u.printStackTrace() }


    }

    @FXML
    private fun saveData() {

    }

    @FXML
    private fun exportImage() {

    }
}