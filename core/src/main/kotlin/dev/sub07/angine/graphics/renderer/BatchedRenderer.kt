package dev.sub07.angine.graphics.renderer

import dev.sub07.angine.event.ResizeEventListener
import dev.sub07.angine.graphics.Camera
import dev.sub07.angine.graphics.primitive.*
import dev.sub07.angine.graphics.send
import dev.sub07.angine.math.div
import dev.sub07.angine.memory.Disposable
import dev.sub07.angine.utils.get

fun <T : BatchedRenderer> T.use(block: T.() -> Unit) {
    begin()
    block()
    end()
}

abstract class BatchedRenderer(
    batchSize: Int, private val components: IntArray, var shader: Shader, private val graphics: Graphics
) : Disposable, ResizeEventListener {
    protected val data: FloatArray = FloatArray(batchSize * components.sum())
    private val vbo = graphics.makeArrayBuffer(data, BufferUsage.Dynamic)
    private val vao = graphics.makeVao(
        components.size,
        components.map { components.sum() * Float.SIZE_BYTES }.toIntArray(),
        getOffsetFromComponents(components),
        components,
        vbo
    )
    protected var drawing: Boolean = false
    var target: Framebuffer = graphics.window
        set(value) {
            if (value != field) {
                if (drawing) {
                    flush()
                }
                onTargetChange(value)
                field = value
            }
        }
    protected var index: Int = 0
        private set

    protected var nbVertices: Int = 0
        private set

    var camera: Camera = Camera(600).also { shader.send(it) }
        set(value) {
            field = value
            shader.send(field)
        }

    init {
        shader.send("flipY", true)
    }

    override fun onResize(width: Int, height: Int) {
        shader.send("projection", 2f / width, 2f / height)
        camera.resize(width, height)
        shader.send(camera)
    }

    fun addVertex(vararg data: Float) {
        check(data.size == components.sum()) { "Data size must be ${components.sum()} but was ${data.size}" }
        data.forEach {
            this.data[index++] = it
        }
        nbVertices++
    }

    fun begin() {
        check(!drawing) { "Renderer already active" }
        this.drawing = true
    }

    fun end() {
        check(drawing) { "Renderer not drawing, call begin first" }
        flush()
        this.drawing = false
    }

    protected fun flush() {
        check(drawing) { "Renderer is not active" }
        if (index == 0) return
        target.bind()
        vao.bind()
        vbo.upload(data[0 until index])
        flushImpl()
        index = 0
        nbVertices = 0
    }

    protected abstract fun flushImpl()

    private fun onTargetChange(newTarget: Framebuffer) {
        if (newTarget == graphics.window) {
            shader.send("flipY", true)
            shader.send(camera)
        }
        shader.send("viewportSize", 2 / newTarget.texture.size)
        shader.send("flipY", newTarget == graphics.window)
        onTargetChangeImpl(newTarget)
    }

    protected open fun onTargetChangeImpl(newTarget: Framebuffer) = Unit

    protected fun check(message: String = "Renderer not active") {
        check(drawing) { message }
    }

    override fun dispose() {
        shader.dispose()
        vao.dispose()
        vbo.dispose()
    }

    companion object {
        private fun getOffsetFromComponents(components: IntArray): LongArray {
            val offsets = LongArray(components.size)
            var offset = 0L
            for (i in components.indices) {
                offsets[i] = offset
                offset += components[i] * Float.SIZE_BYTES
            }
            return offsets
        }
    }
}

const val commonVertexShader = """
    #version 460
    
    layout(location = 0) in vec3 vertexPosition;
    layout(location = 1) in vec4 color;
    
    out vec4 tint;
    uniform bool flipY = true;
    uniform vec2 projection;
    uniform vec2 offset = vec2(0.0);
    
    void main() {
        vec2 v = (vertexPosition.xy - offset) * projection;
        
        if (flipY){
             v.y = -v.y;
        }
        
        gl_Position = vec4(v, vertexPosition.z, 1.0);
        tint = color;
    }
"""