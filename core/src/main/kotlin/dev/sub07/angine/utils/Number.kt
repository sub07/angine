package dev.sub07.angine.utils

import kotlin.math.sqrt

val Number.d: Double get() = toDouble()
val Number.f: Float get() = toFloat()
val Number.l: Long get() = toLong()
val Number.i: Int get() = toInt()
val Number.b: Byte get() = toByte()
val Number.s: Short get() = toShort()
fun Float.sqrt(): Float = sqrt(this)
fun map(
    value: Number,
    minSrc: Number,
    maxSrc: Number,
    minDest: Number,
    maxDest: Number,
) = (minDest.f + (value.f - minSrc.f) * (maxDest.f - minDest.f) / (maxSrc.f - minSrc.f))