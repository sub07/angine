package dev.mpardo.angine.math

import dev.mpardo.angine.memory.Floats
import dev.mpardo.angine.memory.MutableFloats
import dev.mpardo.angine.memory.floats
import dev.mpardo.angine.utils.add
import dev.mpardo.angine.utils.f
import dev.mpardo.angine.utils.sqrt
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

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

object PolygonUtils {
    fun isPointInTriangle(
        xa: Float, ya: Float, xb: Float, yb: Float, xc: Float, yc: Float, px: Float, py: Float
    ): Boolean {
        val d1 = Geometry.det(xb - xa, yb - ya, px - xa, py - ya)
        val d2 = Geometry.det(xc - xb, yc - yb, px - xb, py - yb)
        val d3 = Geometry.det(xa - xc, ya - yc, px - xc, py - yc)
        return if (d1 > 0) d2 > 0 && d3 > 0 else d2 < 0 && d3 < 0
    }
    
    private fun isEar(vertices: Floats, xa: Float, ya: Float, xb: Float, yb: Float, xc: Float, yc: Float): Boolean {
        if (Geometry.det(xa - xb, ya - yb, xc - xb, yc - yb) < 0) return false
        for (i in vertices.indices step 2) {
            val x = vertices[i]
            val y = vertices[i + 1]
            if ((x == xa && y == ya) || (x == xb && y == yb) || (x == xc && y == yc)) continue
            if (isPointInTriangle(xa, ya, xb, yb, xc, yc, x, y)) return false
        }
        return true
    }
    
    fun triangulate(vertices: Floats): MutableFloats {
        val res = mutableListOf<Float>()
        
        val nextMap = (0..vertices.size - 2).filter { it % 2 == 0 }.associateWith { it + 2 }.toMutableMap()
        nextMap[vertices.size - 2] = 0
        
        val next = { i: Int -> nextMap[i]!! }
        val doubleNext = { i: Int -> next(next(i)) }
        
        while (nextMap.size > 2) {
            
            val startIndex = nextMap.keys.first()
            var index = startIndex
            
            do {
                val xa = vertices[index]
                val ya = vertices[index + 1]
                val xb = vertices[next(index)]
                val yb = vertices[next(index) + 1]
                val xc = vertices[doubleNext(index)]
                val yc = vertices[doubleNext(index) + 1]
                
                if (isEar(vertices, xa, ya, xb, yb, xc, yc)) {
                    val toDel = next(index)
                    nextMap[index] = doubleNext(index)
                    nextMap.remove(toDel)
                    res.add(xa, ya, xb, yb, xc, yc)
                    break
                }
                index = next(index)
            } while (next(index) != startIndex)
        }
        
        return floats(res)
    }
    
    fun ellipsePoints(rx: Number, ry: Number = rx, nbEdges: Int = 40): MutableFloats {
        return arcPoints(rx, ry, TwoPi, nbEdges)
    }
    
    fun arcPoints(rx: Number, ry: Number, radian: Float, nbEdges: Int = 40): MutableFloats {
        val res = floats(nbEdges * 2)
        var rad = radian
        val inc = radian / nbEdges
        repeat(nbEdges) {
            res[2 * it] = cos(rad) * rx.f
            res[2 * it + 1] = sin(rad) * ry.f
            rad -= inc
        }
        return res
    }
    
    fun rectPoints(x: Number, y: Number, w: Number, h: Number) = floats(
        x.f, y.f,
        x.f + w.f, y.f,
        x.f + w.f, y.f + h.f,
        x.f, y.f + h.f,
    )
    
    fun convertPointListToLines(vertices: Floats, polyline: Boolean): Floats {
        val res = floats(vertices.size * 2 - if (polyline) 4 else 0)
        var resIndex = 0
        for (i in 0 until vertices.size - 3 step 2) {
            res[resIndex++] = vertices[i]
            res[resIndex++] = vertices[i + 1]
            res[resIndex++] = vertices[i + 2]
            res[resIndex++] = vertices[i + 3]
        }
        
        if (!polyline) {
            res[resIndex++] = vertices[vertices.size - 2]
            res[resIndex++] = vertices[vertices.size - 1]
            res[resIndex++] = vertices[0]
            res[resIndex] = vertices[1]
        }
        
        return res
    }
}