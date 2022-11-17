package dev.sub07.angine.graphics.primitive

import dev.sub07.angine.event.ResizeEventListener
import dev.sub07.angine.graphics.Color
import dev.sub07.angine.graphics.PixelFormat
import dev.sub07.angine.math.Vec
import dev.sub07.angine.math.vec
import dev.sub07.angine.memory.Disposable
import java.nio.ByteBuffer

interface Graphics : GraphicObjectFactory {
    val graphicVendor: String
    val window: Framebuffer
    fun setViewport(x: Int, y: Int, w: Int, h: Int)
    fun initialize(debugMode: Boolean = false)
    fun clear(color: Color? = null)
    fun draw(primitive: DrawPrimitive, nbData: Int, indexed: Boolean)
}

interface GpuObject : Disposable

interface Computed<T> {
    var value: T
}

interface ResizableObject<T> : Computed<T>, ResizeEventListener

fun GpuObject.bind() {
    if (this is Bindable) {
        this.bind()
    }
}

fun GpuObject.unbind() {
    if (this is Bindable) {
        this.unbind()
    }
}

interface Bindable {
    fun bind()
    fun unbind()
}

fun Bindable.use(block: () -> Unit) {
    bind()
    block()
    unbind()
}

typealias ArrayBuffer = GpuBuffer<FloatArray>
typealias IndexBuffer = GpuBuffer<IntArray>

interface GpuBuffer<T> : GpuObject {
    fun upload(data: T)
}

interface Vao : GpuObject

interface Texture : GpuObject {
    val width: Int
    val height: Int
    val filter: TextureFilter
    val wrap: TextureWrap
    val format: PixelFormat
    
    fun bind(textureUnit: Int)
    fun fetchData(): ByteBuffer
    fun sendData(data: ByteBuffer)
}

interface Shader : GpuObject {
    fun <T> send(name: String, value: T)
    fun <T : Number> send(name: String, x: T, y: T)
    fun <T : Number> send(name: String, x: T, y: T, z: T)
    fun <T : Number> send(name: String, x: T, y: T, z: T, w: T)
    fun send(name: String, vec: Array<Vec>)
    fun send(name: String, colors: Array<Color>)
}

interface Framebuffer : GpuObject {
    val width: Int
    val height: Int
    val texture: Texture
    fun clear(color: Color? = null)
}

val Framebuffer.size: Vec
    get() = vec(width, height)

val Texture.size: Vec
    get() = vec(width, height)