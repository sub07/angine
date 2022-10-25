package dev.mpardo.angine.graphics

import dev.mpardo.angine.graphics.primitive.Shader
import dev.mpardo.angine.math.MutableVec
import dev.mpardo.angine.math.Vec
import dev.mpardo.angine.math.vec
import dev.mpardo.angine.utils.f
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Camera(radius: Number) : KoinComponent {
    val window by inject<Window>()
    val radius: Float = radius.f
    var projectionY: Float = 1f / this.radius
    var projectionX: Float = projectionY * window.aspectRatio
    var offsetX: Float = 0f
    var offsetY: Float = 0f
    
    fun transformPositionX(value: Number): Float {
        val clip = value.f * 2f / window.width - 1
        return clip / projectionX + offsetX
    }
    
    fun transformPositionY(value: Number): Float {
        val clip = value.f * 2f / window.height - 1
        return clip / projectionY + offsetY
    }
    
    fun transformSizeX(value: Number): Float {
        return value.f * (1 / projectionX) / (window.width / 2f)
    }
    
    fun transformSizeY(value: Number): Float {
        return value.f * radius / (window.height / 2f)
    }
    
    fun resize(width: Int, height: Int) {
        projectionX = projectionY * (height / width.f)
    }
}

var Camera.projection: Vec
    get() = vec(projectionX, projectionY)
    set(value) {
        projectionX = value.xc
        projectionY = value.yc
    }

var Camera.offset: Vec
    get() = vec(offsetX, offsetY)
    set(value) {
        offsetX = value.xc
        offsetY = value.yc
    }

fun Camera.transformPosition(x: Number, y: Number): Vec = transformPosition(vec(x, y))
fun Camera.transformPosition(vec: MutableVec): MutableVec = vec.set(transformPositionX(vec.xc), transformPositionY(vec.yc))

fun Camera.transformSize(x: Number, y: Number): MutableVec = transformSize(vec(x, y))
fun Camera.transformSize(vec: MutableVec): MutableVec = vec.set(transformSizeX(vec.xc), transformSizeY(vec.yc))

fun Shader.send(camera: Camera) {
    send("projection", camera.projectionX, camera.projectionY)
    send("offset", camera.offsetX, camera.offsetY)
}