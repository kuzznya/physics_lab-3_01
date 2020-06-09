package com.kuzznya.lab.model

import kotlin.math.pow
import kotlin.math.sqrt

class TensionField (
    private val potentialData: Map<Point, Double>
) {
    fun getTension(x: Double, y: Double): Double {
        val pointBeforeByX = potentialData.keys.filter { it.x < x }
            .minBy { (it.x - x).pow(2) + (it.y - y).pow(2) }
            ?: return 0.0

        val pointAfterByX = potentialData.keys.filter { it.x > x }
            .minBy { (it.x - x).pow(2) + (it.y - y).pow(2) }
            ?: return 0.0

        val pointBeforeByY = potentialData.keys.filter { it.y < y }
            .minBy { (it.x - x).pow(2) + (it.y - y).pow(2) }
            ?: return 0.0

        val pointAfterByY = potentialData.keys.filter { it.y > y }
            .minBy { (it.x - x).pow(2) + (it.y - y).pow(2) }
            ?: return 0.0

        val ex = - ((potentialData[pointBeforeByX] ?: return(0.0)) -
                (potentialData[pointAfterByX] ?: return(0.0))) /
                (pointAfterByX.x - pointBeforeByX.x)

        val ey = - ((potentialData[pointBeforeByY] ?: return(0.0)) -
                (potentialData[pointAfterByY] ?: return(0.0))) /
                (pointAfterByY.y - pointBeforeByY.y)

        return sqrt(ex.pow(2) + ey.pow(2))
    }
}