package dev.mpardo.angine.graphics.primitive

import dev.mpardo.angine.graphics.PixelFormat
import java.nio.ByteBuffer

interface GraphicObjectFactory {
    fun makeIndexBuffer(data: IntArray, usage: BufferUsage): GpuBuffer<IntArray>
    fun makeArrayBuffer(data: FloatArray, usage: BufferUsage): GpuBuffer<FloatArray>
    fun makeVao(n: Int, strides: IntArray, offsets: LongArray, sizes: IntArray, vbo: ArrayBuffer): Vao
    fun makeTexture(
        width: Int,
        height: Int,
        format: PixelFormat,
        data: ByteBuffer,
        filter: TextureFilter = TextureFilter.Linear,
        wrap: TextureWrap = TextureWrap.Repeat
    ): Texture
    
    fun makeFramebuffer(width: Int, height: Int): Framebuffer
    fun makeAutoResizedFramebuffer(width: Int, height: Int): Framebuffer
    fun makeShader(vertexSources: String, fragmentSources: String): Shader
}
