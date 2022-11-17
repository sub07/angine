package dev.sub07.angine.memory

import dev.sub07.angine.math.MutableVec
import dev.sub07.angine.math.Vec
import dev.sub07.angine.math.set
import dev.sub07.angine.math.vec

interface VecPool : Pool<MutableVec> {
    fun get(x: Number, y: Number = x): MutableVec
    fun get(vec: Vec): MutableVec
}

class StrictVecPool : StrictPool<MutableVec>({ it.set(0f, 0f) }, 100), VecPool {
    override fun create() = vec()
    override fun get(x: Number, y: Number) = pool().apply { set(x, y) }
    override fun get(vec: Vec) = pool().apply { set(vec) }
}

class SimpleVecPool : SimplePool<MutableVec>(100), VecPool {
    override fun create() = vec()
    override fun get(x: Number, y: Number) = pool().apply { set(x, y) }
    override fun get(vec: Vec) = pool().apply { set(vec) }
}