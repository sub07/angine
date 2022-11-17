package dev.sub07.angine.collection

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

interface BitSet {
    val trueIndexes: Collection<Int>
    val size: Int
    operator fun get(i: Int): Boolean
    fun containsAtLeast(other: BitSet): Boolean
    fun intersects(other: BitSet): Boolean
}

interface MutableBitSet : BitSet {
    operator fun set(i: Int, value: Boolean)
    fun clear()
}

class BooleanArrayBitSet : MutableBitSet {
    private var data = booleanArrayOf()
    
    override val size get() = data.size
    
    private fun ensure(size: Int) {
        if (size < data.size) return
        data = data.copyOf(size)
    }
    
    override val trueIndexes: Collection<Int> get() = data.withIndex().filter { it.value }.map { it.index }
    
    override fun get(i: Int): Boolean {
        ensure(i + 1)
        return data[i]
    }
    
    override fun set(i: Int, value: Boolean) {
        ensure(i + 1)
        data[i] = value
    }
    
    override fun clear() {
        for (i in data.indices) {
            data[i] = false
        }
    }
    
    override fun containsAtLeast(other: BitSet): Boolean {
        val maxSize = maxOf(size, other.size)
        ensure(maxSize)
        other[maxSize]
        return (0 until maxSize).all { data[it] == other[it] }
    }
    
    override fun intersects(other: BitSet): Boolean {
        val maxSize = maxOf(size, other.size)
        ensure(maxSize)
        other[maxSize]
        return (0 until maxSize).any { data[it] == other[it] }
    }
}

val booleanArrayBitSetModule = module {
    factory<MutableBitSet> { BooleanArrayBitSet() }
}

fun KoinComponent.mutableBitSet() = get<MutableBitSet>()