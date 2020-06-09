package com.kuzznya.lab.service

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.TensionField
import javafx.beans.Observable
import javafx.beans.value.ChangeListener
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow

class ComputationDisplay (
    private val field: TensionField,
    private val canvas: Canvas,
    private val minTension: Double,
    private val maxTension: Double,
    private val from: Point,
    private val to: Point
) {
    private var data: ArrayList<ArrayList<Double>> = ArrayList()

    private var lastDrawedWidth = 0
    private var lastDrawedHeight = 0

    fun compute(): CompletableFuture<Unit> {
        val xMin = from.x
        val xMax = to.x
        val yMin = from.y
        val yMax = to.y

        val ctx = canvas.graphicsContext2D

        for (y in 0..canvas.height.toInt() step 3) {
            data.add(ArrayList())
            for (x in 0..canvas.width.toInt() step 3) {
                val tensionValue = field.getTension(
                    x / canvas.width * (xMax - xMin) + xMin,
                    y / canvas.height * (yMax - yMin) + yMin
                )
                data.last().add(tensionValue)

                val hue = min(((tensionValue - minTension) / (maxTension - minTension)).pow(1 / 1.2) * (360.0 - 240.0) + 240.0, 360.0)
                ctx.fill = Color.hsb(hue, 1.0, 1.0)
                ctx.fillRect(x.toDouble() - 1.5, y.toDouble() - 1.5, 3.0, 3.0)
            }
        }

        lastDrawedWidth = canvas.width.toInt()
        lastDrawedHeight = canvas.height.toInt()

        canvas.widthProperty().addListener { _, _, newValue ->
            if (abs(newValue.toInt() - lastDrawedWidth) > 10 ) {
                Router.primaryStage.isResizable = false
                redraw().get()
                Router.primaryStage.isResizable = true
                lastDrawedWidth = newValue.toInt()
            }
        }
        canvas.heightProperty().addListener { _ , _, newValue ->
            if (abs(newValue.toInt() - lastDrawedHeight) > 10 ) {
                Router.primaryStage.isResizable = false
                redraw().get()
                Router.primaryStage.isResizable = true
                lastDrawedHeight = newValue.toInt()
            }
        }

        return CompletableFuture.completedFuture(Unit)
    }

    fun redraw(): CompletableFuture<Unit> {
        if (data.isEmpty())
            return CompletableFuture.failedFuture(RuntimeException())

        val ctx = canvas.graphicsContext2D

        ctx.fill = Color.hsb(240.0, 1.0, 1.0)
        ctx.fillRect(0.0, 0.0, canvas.width, canvas.height)

        for (y in data.indices) {
            for (x in data[y].indices) {
                val tensionValue = data[y][x]
                val hue = min(((tensionValue - minTension) / (maxTension - minTension)).pow(1 / 1.2) * (360.0 - 240.0) + 240.0, 360.0)
                ctx.fill = Color.hsb(hue, 1.0, 1.0)
                ctx.fillRect(x / data[y].size.toDouble() * canvas.width - canvas.width / data[y].size / 2.0,
                    y / data.size.toDouble() * canvas.height - canvas.height / data.size / 2.0,
                    canvas.width / data[y].size,
                    canvas.height / data.size
                )
            }
        }

        return CompletableFuture.completedFuture(Unit)
    }
}