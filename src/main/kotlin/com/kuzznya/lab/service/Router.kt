package com.kuzznya.lab.service

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.net.URL

object Router {
    lateinit var primaryStage: Stage

    private val fxmlPath = "fxml/"

    private val views: Map<String, String> = mapOf(
        "Main" to "main.fxml"
    )

    private fun getFxmlUrl(name: String): URL? =
        javaClass.classLoader.getResource(fxmlPath + name)?.toURI()?.toURL()

    fun loadView(name: String) {
        val loader = FXMLLoader(
            getFxmlUrl(
                views[name] ?: throw ClassNotFoundException()
            )
        )
        val root = loader.load<Parent>()
        val scene = Scene(root)
        primaryStage.scene = scene
        primaryStage.show()
    }

}