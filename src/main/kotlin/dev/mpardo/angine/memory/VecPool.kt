package dev.mpardo.angine.memory

import dev.mpardo.angine.math.MutableVec
import dev.mpardo.angine.math.Vec
import dev.mpardo.angine.math.set
import dev.mpardo.angine.math.vec

interface VecPool : Pool<MutableVec> {
    fun get(x: Number, y: Number = x): MutableVec
    fun get(vec: Vec): MutableVec
}

private object StrictVecPool : StrictPool<MutableVec>({ it.set(0f, 0f) }, 100), VecPool {
    override fun create() = vec()
    override fun get(x: Number, y: Number) = pool().apply { set(x, y) }
    override fun get(vec: Vec) = pool().apply { set(vec) }
}

private object SimpleVecPool : SimplePool<MutableVec>(100), VecPool {
    override fun create() = vec()
    override fun get(x: Number, y: Number) = pool().apply { set(x, y) }
    override fun get(vec: Vec) = pool().apply { set(vec) }
}