package dev.sub07.angine.graphics

import dev.sub07.angine.utils.f

object ZIndex {
    const val maxValue = 10_000
    var value = 0
        set(value) {
            field = value.coerceAtMost(maxValue)
        }
    val ratio: Float
        get() {
            return if (value > maxValue) 1f
            else value.f / maxValue
        }
}