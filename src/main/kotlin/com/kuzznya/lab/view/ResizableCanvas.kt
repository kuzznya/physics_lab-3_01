package com.kuzznya.lab.view

import javafx.scene.canvas.Canvas

class ResizableCanvas : Canvas() {
    override fun prefWidth(height: Double) = 0.0
    override fun prefHeight(width: Double) = 0.0
}