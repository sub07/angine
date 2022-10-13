package dev.mpardo.angine.math

import dev.mpardo.angine.utils.f
import dev.mpardo.angine.utils.sqrt
import kotlin.math.atan2

val Number.deg: Float get() = this.f * 180f / Pi
val Number.rad: Float get() = this.f * Pi / 180f

object Geometry {
    fun dist2(x1: Number, y1: Number, x2: Number, y2: Number): Float = (x1.f - x2.f) * (x1.f - x2.f) + (y1.f - y2.f) * (y1.f - y2.f)
    
    fun dist(x1: Number, y1: Number, x2: Number, y2: Number): Float = dist2(x1.f, y1.f, x2.f, y2.f).sqrt()
    
    fun det(x1: Number, y1: Number, x2: Number, y2: Number): Float = x1.f * y2.f - y1.f * x2.f
    
    fun dot(x1: Number, y1: Number, x2: Number, y2: Number): Float = x1.f * x2.f + y1.f * y2.f
    
    fun norm2(x: Number, y: Number): Float = x.f * x.f + y.f * y.f
    
    fun norm(x: Number, y: Number): Float = norm2(x, y).sqrt()
    
    /**
     * Returns the angle between two vectors.
     * The two vectors must be like this : *---->*---->
     * Not like that : <----**---->
     * Or like that : *----><----*
     */
    fun angle(x1: Number, y1: Number, x2: Number, y2: Number): Float {
        val det = det(x1, y1, x2, y2)
        val dot = dot(x1, y1, x2, y2)
        val angle = atan2(det, dot) // [-Pi, Pi]
        return TwoPi - (angle + Pi) // [0, 2Pi] (anticlockwise)
    }
    
    fun angle(x1: Number, y1: Number, x2: Number, y2: Number, x3: Number, y3: Number): Float =
        angle(x2.f - x1.f, y2.f - y1.f, x3.f - x2.f, y3.f - y2.f)
}