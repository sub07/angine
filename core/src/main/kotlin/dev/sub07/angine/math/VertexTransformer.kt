package dev.sub07.angine.math

import dev.sub07.angine.memory.Floats
import dev.sub07.angine.memory.MutableFloats
import dev.sub07.angine.memory.borrow
import dev.sub07.angine.memory.floats

class VertexTransformer(
    var translation: MutableVec = vec(),
    var scale: MutableVec = vec(1),
    var rotation: Float = 0f,
    var origin: MutableVec = vec(0),
) {
    fun transform(input: MutableVec) {
        input -= origin
        input *= scale
        input.rotate(rotation)
        input += translation
    }
    
    fun transformed(input: Vec): MutableVec = input.copy().apply {
        transform(this)
    }
    
    fun transformed(vertices: Floats): Floats {
        val res = floats(vertices.size)
        vecPool.borrow {
            for (i in vertices.indices step 2) {
                it.set(vertices[i], vertices[i + 1])
                transform(it)
                res[i] = it.x
                res[i + 1] = it.y
            }
        }
        return res
    }
    
    fun transform(vertices: MutableFloats): MutableFloats {
        vecPool.borrow {
            for (i in vertices.indices step 2) {
                it.set(vertices[i], vertices[i + 1])
                transform(it)
                vertices[i] = it.x
                vertices[i + 1] = it.y
            }
        }
        return vertices
    }
}