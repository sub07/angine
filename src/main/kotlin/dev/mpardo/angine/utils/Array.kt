package dev.mpardo.angine.utils

import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer

fun ByteBuffer.isEmpty(): Boolean = limit() == 0
fun ByteBuffer.isNotEmpty(): Boolean = !isEmpty()
fun emptyByteBuffer(): ByteBuffer = MemoryUtil.memCalloc(0)
fun ByteBuffer.toByteArray(): ByteArray = ByteArray(capacity()) { this[it] }
fun ByteArray.toByteBuffer(): ByteBuffer = MemoryUtil.memAlloc(size).also { it.put(this) }.flip()

fun MemoryStack.malloc(): IntBuffer = mallocInt(1)
val IntBuffer.value: Int get() = get(0)

operator fun FloatArray.get(range: IntRange) = sliceArray(range)

fun <T> MutableCollection<T>.add(vararg items: T) {
    items.forEach { add(it) }
}