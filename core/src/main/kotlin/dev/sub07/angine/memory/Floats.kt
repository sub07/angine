package dev.sub07.angine.memory

import dev.sub07.angine.math.MutableVec
import dev.sub07.angine.math.Vec
import dev.sub07.angine.math.vecPool

interface Floats : Iterable<Float> {
    val size: Int
    val indices: IntRange
    val array: FloatArray
    operator fun get(index: Int): Float
    operator fun get(range: IntRange): MutableFloats = slice(range)
    fun copy(): MutableFloats
    fun slice(indices: IntRange): MutableFloats
    infix fun eq(other: Floats): Boolean
    infix fun neq(other: Floats): Boolean
}

interface MutableFloats : Floats {
    val data: FloatArray
    operator fun set(index: Int, value: Float)
    operator fun set(index: Int, value: Vec)
}

private class FloatsImpl(override val data: FloatArray) : Floats, MutableFloats {
    override fun set(index: Int, value: Float) {
        data[index] = value
    }
    
    override fun set(index: Int, value: Vec) {
        data[index] = value.xc
        data[index + 1] = value.yc
    }
    
    override val size: Int = data.size
    override val indices: IntRange = data.indices
    override val array: FloatArray = data.clone()
    
    override fun get(index: Int): Float = data[index]
    
    override fun copy(): MutableFloats = FloatsImpl(data.clone())
    
    override fun slice(indices: IntRange): MutableFloats = FloatsImpl(data.sliceArray(indices))
    
    override infix fun eq(other: Floats): Boolean {
        if (other.size != size) return false
        for (i in indices) {
            if (data[i] != other[i]) return false
        }
        return true
    }
    
    override infix fun neq(other: Floats): Boolean = !eq(other)
    
    override fun iterator(): Iterator<Float> = data.iterator()
}

fun floats(size: Int): MutableFloats = FloatsImpl(FloatArray(size))
fun floats(vararg values: Float): MutableFloats = FloatsImpl(values)
fun floats(values: Collection<Float>): MutableFloats = FloatsImpl(values.toFloatArray())
fun floats(size: Int, init: (Int) -> Float): MutableFloats = FloatsImpl(FloatArray(size, init))

fun Collection<Vec>.toFloats(): MutableFloats {
    val array = FloatArray(size * 2)
    var i = 0
    for (vec in this) {
        array[i++] = vec.xc
        array[i++] = vec.yc
    }
    return floats(*array)
}

fun MutableFloats.transform(transform: (vec: MutableVec) -> Unit): MutableFloats {
    vecPool.borrow {
        for (i in indices step 2) {
            transform(it.set(this[i], this[i + 1]))
            this[i] = it.x
            this[i + 1] = it.y
        }
    }
    return this
}

fun Floats.checkEnough(minSize: Int) = check(size >= minSize) { "Array is too small" }

inline fun Floats.forEachVertex(block: (x: Float, y: Float) -> Unit) {
    checkEnough(2)
    for (i in indices step 2) {
        block(this[i], this[i + 1])
    }
}

inline fun Floats.forEachPair(loop: Boolean = true, block: (x1: Float, y1: Float, x2: Float, y2: Float) -> Unit) {
    checkEnough(4)
    for (i in indices step 4) {
        block(this[i], this[i + 1], this[i + 2], this[i + 3])
    }
    if (loop) {
        block(this[size - 2], this[size - 1], this[0], this[1])
    }
}

inline fun Floats.forEachTriangle(loop: Boolean = false, block: (x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) -> Unit) {
    checkEnough(6)
    for (i in indices step 6) {
        block(this[i], this[i + 1], this[i + 2], this[i + 3], this[i + 4], this[i + 5])
    }
    if (loop) {
        block(this[size - 4], this[size - 3], this[size - 2], this[size - 1], this[0], this[1])
        block(this[size - 2], this[size - 1], this[0], this[1], this[2], this[3])
    }
}

inline fun Floats.forEachQuad(
    loop: Boolean = false, block: (x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float) -> Unit
) {
    checkEnough(8)
    for (i in indices step 8) {
        block(this[i], this[i + 1], this[i + 2], this[i + 3], this[i + 4], this[i + 5], this[i + 6], this[i + 7])
    }
    if (loop) {
        block(this[size - 6], this[size - 5], this[size - 4], this[size - 3], this[size - 2], this[size - 1], this[0], this[1])
        block(this[size - 4], this[size - 3], this[size - 2], this[size - 1], this[0], this[1], this[2], this[3])
        block(this[size - 2], this[size - 1], this[0], this[1], this[2], this[3], this[4], this[5])
    }
}