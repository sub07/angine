package dev.mpardo.angine.graphics.primitive

import dev.mpardo.angine.event.ResizeEventManager
import dev.mpardo.angine.graphics.Color
import dev.mpardo.angine.graphics.PixelFormat
import dev.mpardo.angine.graphics.Window
import dev.mpardo.angine.math.Vec
import dev.mpardo.angine.utils.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*
import org.lwjgl.opengl.GLUtil
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class GLGraphics : Graphics, GraphicObjectFactory by GLGraphicObjectFactory() {
    override val graphicVendor: String
        get() = glGetString(GL_VERSION) ?: "Unknown Vendor"
    override val window: Framebuffer
        get() = GLFramebuffer.window
    
    override fun setViewport(x: Int, y: Int, w: Int, h: Int) {
        glViewport(x, y, w, h)
    }
    
    override fun initialize(debugMode: Boolean) {
        GL.createCapabilities()
        if (debugMode) GLUtil.setupDebugMessageCallback()
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)
        glDisable(GL_CULL_FACE)
        clear(Color.Black)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }
    
    override fun clear(color: Color?) {
        color?.let {
            glClearColor(it.r, it.g, it.b, 1f)
        }
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)
    }
    
    override fun draw(primitive: DrawPrimitive, nbData: Int, indexed: Boolean) {
        if (indexed) {
            glDrawElements(primitive.openglValue, nbData, GL_UNSIGNED_INT, 0)
        } else {
            glDrawArrays(primitive.openglValue, 0, nbData)
        }
    }
}

class GLIndexBuffer(data: IntArray, usage: BufferUsage) : IndexBuffer, Bindable {
    private val handle: Int = glCreateBuffers()
    
    init {
        glNamedBufferData(handle, data, usage.openglValue)
    }
    
    override fun dispose() = glDeleteBuffers(handle)
    
    override fun upload(data: IntArray) = glNamedBufferSubData(handle, 0, data)
    
    override fun bind() = bind(handle)
    
    override fun unbind() = bind(0)
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GLIndexBuffer) return false
        
        if (handle != other.handle) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return handle
    }
    
    companion object {
        private var bound: Int = 0
        
        private fun bind(handle: Int) {
            if (handle != bound) {
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, handle)
                bound = handle
            }
        }
    }
}

class GLArrayBuffer(data: FloatArray, usage: BufferUsage) : ArrayBuffer, Bindable {
    internal val handle: Int = glCreateBuffers()
    
    init {
        glNamedBufferData(handle, data, usage.openglValue)
    }
    
    override fun dispose() = glDeleteBuffers(handle)
    
    override fun upload(data: FloatArray) = glNamedBufferSubData(handle, 0, data)
    
    override fun bind() = bind(handle)
    
    override fun unbind() = bind(0)
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GLArrayBuffer) return false
        
        if (handle != other.handle) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return handle
    }
    
    companion object {
        private var bound: Int = 0
        
        private fun bind(handle: Int) {
            if (handle != bound) {
                glBindBuffer(GL_ARRAY_BUFFER, handle)
                bound = handle
            }
        }
    }
}

class GLVao(n: Int, strides: IntArray, offsets: LongArray, sizes: IntArray, vbo: GLArrayBuffer) : Vao, Bindable {
    private val handle: Int = glCreateVertexArrays()
    
    init {
        for (i in 0 until n) {
            glEnableVertexArrayAttrib(handle, i)
            glVertexArrayVertexBuffer(handle, i, vbo.handle, offsets[i], strides[i])
            glVertexArrayAttribFormat(handle, i, sizes[i], GL_FLOAT, false, 0)
            glVertexArrayAttribBinding(handle, i, i)
        }
    }
    
    override fun dispose() {
        glDeleteVertexArrays(handle)
    }
    
    override fun bind() {
        bind(handle)
    }
    
    override fun unbind() {
        bind(0)
    }
    
    companion object {
        private var bound: Int = 0
        
        private fun bind(handle: Int) {
            if (handle != bound) {
                glBindVertexArray(handle)
                bound = handle
            }
        }
    }
}

class GLTexture(
    override val width: Int,
    override val height: Int,
    override val format: PixelFormat,
    data: ByteBuffer,
    override val filter: TextureFilter = TextureFilter.Linear,
    override val wrap: TextureWrap = TextureWrap.Repeat
) : Texture {
    internal val handle = glCreateTextures(GL_TEXTURE_2D)
    
    init {
        glPixelStorei(GL_UNPACK_ALIGNMENT, alignmentFromNbChannel(format))
        glTextureParameteri(handle, GL_TEXTURE_MIN_FILTER, filter.openglValue)
        glTextureParameteri(handle, GL_TEXTURE_MAG_FILTER, filter.openglValue)
        glTextureParameteri(handle, GL_TEXTURE_WRAP_S, wrap.openglValue)
        glTextureParameteri(handle, GL_TEXTURE_WRAP_T, wrap.openglValue)
        glTextureStorage2D(handle, 1, internalStorageFromNbChannel(format), width, height)
        if (data.isNotEmpty()) glTextureSubImage2D(
            handle, 0, 0, 0, width, height, externalStorageFromNbChannel(format), GL_UNSIGNED_BYTE, data
        )
    }
    
    override fun dispose() = glDeleteTextures(handle)
    
    override fun bind(textureUnit: Int) = glBindTextureUnit(textureUnit, handle)
    
    override fun fetchData(): ByteBuffer {
        val data: ByteBuffer = BufferUtils.createByteBuffer(width * height * format.nbChannel)
        glGetTextureImage(handle, 0, GL_RGBA, GL_UNSIGNED_BYTE, data)
        return data
    }
    
    override fun sendData(data: ByteBuffer) {
        glTextureSubImage2D(handle, 0, 0, 0, width, height, externalStorageFromNbChannel(format), GL_UNSIGNED_BYTE, data)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GLTexture) return false
        
        if (handle != other.handle) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return handle
    }
    
    companion object {
        private fun internalStorageFromNbChannel(pixelFormat: PixelFormat) = when (pixelFormat) {
            PixelFormat.R8 -> GL_R8
            PixelFormat.RGB8 -> GL_RGB8
            PixelFormat.RGBA8 -> GL_RGBA8
        }
        
        private fun externalStorageFromNbChannel(pixelFormat: PixelFormat) = when (pixelFormat) {
            PixelFormat.R8 -> GL_RED
            PixelFormat.RGB8 -> GL_RGB
            PixelFormat.RGBA8 -> GL_RGBA
        }
        
        private fun alignmentFromNbChannel(pixelFormat: PixelFormat) = when (pixelFormat) {
            PixelFormat.R8 -> 1
            PixelFormat.RGB8 -> 1
            PixelFormat.RGBA8 -> 4
        }
    }
}

class ShaderCompileException(val vertexErrorLog: String? = null, val fragmentErrorLog: String? = null) :
    Exception("Failed to compile shaders :\n$vertexErrorLog\n$fragmentErrorLog")

class ShaderLinkageException(val log: String) : Exception("Failed to link shaders :\n$log")

class GLShader(
    vertexSource: String,
    fragmentSource: String,
) : Shader, Bindable {
    private val handle: Int = glCreateProgram()
    
    init {
        val vs = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vs, vertexSource)
        glCompileShader(vs)
        
        if (!isShaderCompilationOk(vs)) {
            throw ShaderCompileException(vertexErrorLog = getShaderCompilationLog(vs))
        }
        
        val fs = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fs, fragmentSource)
        glCompileShader(fs)
        
        if (!isShaderCompilationOk(fs)) {
            throw ShaderCompileException(vertexErrorLog = getShaderCompilationLog(fs))
        }
        
        glAttachShader(handle, vs)
        glAttachShader(handle, fs)
        glLinkProgram(handle)
        
        if (!isProgramLinkageOk(handle)) {
            throw ShaderLinkageException(getProgramLinkageLog(handle))
        }
        
        glDeleteShader(vs)
        glDeleteShader(fs)
    }
    
    override fun dispose() = glDeleteProgram(handle)
    
    override fun bind() = bind(handle)
    
    override fun unbind() = bind(0)
    
    override fun <T> send(name: String, value: T) = applyAndRestore {
        val location = glGetUniformLocation(handle, name)
        when (value) {
            is Int -> glUniform1i(location, value)
            is Float -> glUniform1f(location, value)
            is Double -> glUniform1d(location, value)
            is Boolean -> glUniform1i(location, if (value) 1 else 0)
            is Color -> glUniform4f(
                location, value.r, value.g, value.b, value.a
            )
            is Vec -> glUniform2f(location, value.xc, value.yc)
            is FloatArray -> glUniform4fv(location, value)
            is IntArray -> glUniform1iv(location, value)
        }
    }
    
    override fun <T : Number> send(name: String, x: T, y: T) = applyAndRestore {
        val location = glGetUniformLocation(handle, name)
        when (x) {
            is Int -> glUniform2i(location, x, y.i)
            is Float -> glUniform2f(location, x, y.f)
            is Double -> glUniform2d(location, x, y.d)
        }
    }
    
    override fun <T : Number> send(name: String, x: T, y: T, z: T) = applyAndRestore {
        val location = glGetUniformLocation(handle, name)
        when (x) {
            is Int -> glUniform3i(location, x, y.i, z.i)
            is Float -> glUniform3f(location, x, y.f, z.f)
            is Double -> glUniform3d(location, x, y.d, z.d)
        }
    }
    
    override fun <T : Number> send(name: String, x: T, y: T, z: T, w: T) = applyAndRestore {
        val location = glGetUniformLocation(handle, name)
        when (x) {
            is Int -> glUniform4i(location, x, y.i, z.i, w.i)
            is Float -> glUniform4f(location, x, y.f, z.f, w.f)
            is Double -> glUniform4d(location, x, y.d, z.d, w.d)
        }
    }
    
    override fun send(name: String, vec: Array<Vec>) = applyAndRestore {
        val location = glGetUniformLocation(handle, name)
        glUniform2fv(location, vec.flatMap { listOf(it.xc, it.yc) }.toFloatArray())
    }
    
    override fun send(name: String, colors: Array<Color>) = applyAndRestore {
        val location = glGetUniformLocation(handle, name)
        glUniform4fv(
            location, colors.flatMap { floatArrayOf(it.r, it.g, it.b, it.a).asIterable() }.toFloatArray()
        )
    }
    
    private fun applyAndRestore(block: () -> Unit) {
        val previous = bound
        bind()
        block()
        bind(previous)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GLShader) return false
        
        if (handle != other.handle) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return handle
    }
    
    companion object {
        private var bound: Int = 0
        
        private fun bind(handle: Int) {
            if (bound != handle) {
                glUseProgram(handle)
                bound = handle
            }
        }
        
        private fun isShaderCompilationOk(handle: Int): Boolean = MemoryStack.stackPush().use {
            val compileFlag = it.malloc()
            glGetShaderiv(handle, GL_COMPILE_STATUS, compileFlag)
            compileFlag.value == GL_TRUE
        }
        
        private fun getShaderCompilationLog(handle: Int): String = glGetShaderInfoLog(handle)
        
        private fun isProgramLinkageOk(handle: Int): Boolean = MemoryStack.stackPush().use {
            val linkFlag = it.mallocInt(1)
            glGetProgramiv(handle, GL_LINK_STATUS, linkFlag)
            linkFlag.get(0) == GL_TRUE
        }
        
        private fun getProgramLinkageLog(handle: Int): String = glGetProgramInfoLog(handle)
    }
}

class WindowFramebufferTextureAccessError : Error()

class GLFramebuffer(
    override val width: Int,
    override val height: Int,
) : Framebuffer, Bindable, KoinComponent {
    private val handle: Int = glCreateFramebuffers()
    override val texture: GLTexture = GLTexture(width, height, PixelFormat.RGBA8, emptyByteBuffer())
    private val primitives by inject<Graphics>()
    
    init {
        glBindFramebuffer(GL_FRAMEBUFFER, handle)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.handle, 0)
        check(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) { "FBO creation error" }
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }
    
    override fun dispose() {
        glDeleteFramebuffers(handle)
        texture.dispose()
    }
    
    override fun bind() {
        bind(handle)
    }
    
    override fun unbind() {
        bind(0)
    }
    
    override fun clear(color: Color?) {
        val previous = bound
        bind()
        primitives.clear(color)
        bind(previous)
    }
    
    companion object {
        private var bound: Int = 0
        
        private fun bind(handle: Int) {
            if (handle != bound) {
                glBindFramebuffer(GL_FRAMEBUFFER, handle)
                bound = handle
            }
        }
        
        val window: Framebuffer by lazy {
            object : Framebuffer, Bindable, KoinComponent {
                private val primitives by inject<Graphics>()
                private val window by inject<Window>()
                override val width: Int get() = window.width
                override val height: Int get() = window.height
                override val texture: Texture
                    get() = throw WindowFramebufferTextureAccessError()
                
                override fun clear(color: Color?) {
                    val previous = bound
                    bind()
                    primitives.clear(color)
                    bind(previous)
                }
                
                override fun dispose() = Unit
                
                override fun bind() = bind(0)
                
                override fun unbind() = bind(0)
            }
        }
    }
}

class ResizableGLFramebuffer(initial: Framebuffer, val create: (Int, Int) -> Framebuffer) : ResizableObject<Framebuffer>, Framebuffer, Bindable {
    override var value: Framebuffer = initial
    
    override fun onResize(width: Int, height: Int) {
        dispose()
        value = create(width, height)
    }
    
    override val width: Int get() = value.width
    override val height: Int get() = value.height
    override val texture: Texture get() = value.texture
    override fun clear(color: Color?) = value.clear(color)
    override fun dispose() = value.dispose()
    override fun bind() = value.bind()
    override fun unbind() = value.unbind()
}

class GLGraphicObjectFactory : GraphicObjectFactory {
    override fun makeIndexBuffer(data: IntArray, usage: BufferUsage): GpuBuffer<IntArray> {
        return GLIndexBuffer(data, usage)
    }
    
    override fun makeArrayBuffer(data: FloatArray, usage: BufferUsage): GpuBuffer<FloatArray> {
        return GLArrayBuffer(data, usage)
    }
    
    override fun makeVao(n: Int, strides: IntArray, offsets: LongArray, sizes: IntArray, vbo: ArrayBuffer): Vao {
        return GLVao(n, strides, offsets, sizes, vbo as GLArrayBuffer)
    }
    
    override fun makeTexture(width: Int, height: Int, format: PixelFormat, data: ByteBuffer, filter: TextureFilter, wrap: TextureWrap): Texture {
        return GLTexture(width, height, format, data, filter, wrap)
    }
    
    override fun makeFramebuffer(width: Int, height: Int): Framebuffer {
        return GLFramebuffer(width, height)
    }
    
    override fun makeAutoResizedFramebuffer(width: Int, height: Int): Framebuffer {
        return ResizableGLFramebuffer(makeFramebuffer(width, height)) { w, h ->
            makeFramebuffer(w, h)
        }.also { ResizeEventManager += it }
    }
    
    override fun makeShader(vertexSources: String, fragmentSources: String): Shader {
        return GLShader(vertexSources, fragmentSources)
    }
}
