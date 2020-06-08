package com.kuzznya.lab.service

import com.kuzznya.lab.model.Point
import java.io.File
import java.util.*

class DataReader(var path: String) {
    fun loadData(): Map<Point, Double> {
        val data: MutableMap<Point, Double> = emptyMap<Point, Double>().toMutableMap()
        File(path).forEachLine {
            if (it.startsWith('%'))
                return@forEachLine

            val scanner = Scanner(it).useLocale(Locale.US).useDelimiter("[ +-]+")

            val x: Double
            val y: Double
            val value: Double

            if (scanner.hasNextDouble())
                x = scanner.nextDouble()
            else
                return@forEachLine

            if (scanner.hasNextDouble())
                y = scanner.nextDouble()
            else
                return@forEachLine

            if (scanner.hasNextDouble())
                value = scanner.nextDouble()
            else
                return@forEachLine

            data[Point(x, y)] = value
        }
        return data
    }
}