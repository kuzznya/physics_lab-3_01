package com.kuzznya.lab.service

import com.kuzznya.lab.model.Point
import java.io.File
import java.util.*
import kotlin.math.pow

class DataReader(var path: String) {
    fun loadData(): Map<Point, Double> {
        val data: MutableMap<Point, Double> = emptyMap<Point, Double>().toMutableMap()
        File(path).forEachLine {
            if (it.startsWith('%'))
                return@forEachLine

            val scanner = Scanner(it).useLocale(Locale.US)

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

            val str = scanner.next()
            value = kotlin.runCatching {
                var valueStr = str.split('+', '-')[0]
                if (valueStr.last() == 'E')
                    valueStr += str.split('+', '-')[1]
                return@runCatching valueStr.toDouble()
            } .getOrDefault(0.0)

            data[Point(x, y)] = value
        }
        return data
    }
}