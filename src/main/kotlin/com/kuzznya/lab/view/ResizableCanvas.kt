package com.kuzznya.lab.view

import javafx.scene.canvas.Canvas


class ResizableCanvas : Canvas() {
    override fun isResizable() = true
    override fun prefWidth(height: Double) = 100.0
    override fun prefHeight(width: Double) = 100.0
}