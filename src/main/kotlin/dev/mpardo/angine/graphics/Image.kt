package dev.mpardo.angine.graphics

import dev.mpardo.angine.utils.i
import java.nio.ByteBuffer

enum class PixelFormat(val nbChannel: Int) {
    R8(1),
    RGB8(3),
    RGBA8(4);
    
    companion object {
        fun fromNbChannel(nbChannel: Int) = when (nbChannel) {
            1 -> R8
            3 -> RGB8
            4 -> RGBA8
            else -> error("Invalid number of channels: $nbChannel")
        }
    }
}

interface Image {
    val width: Int
    val height: Int
    val format: PixelFormat
    val pixels: ByteArray
    
    operator fun get(x: Int, y: Int): Color
    operator fun get(index: Int): Byte
    operator fun set(x: Int, y: Int, value: Color)
    operator fun set(index: Int, value: Byte)
    fun drawImage(x: Int, y: Int, image: Image)
    fun fill(color: Color)
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Color)
}

class MatrixImage(
    override val width: Int,
    override val height: Int,
    override val format: PixelFormat,
) : Image {
    override val pixels: ByteArray = ByteArray(width * height * format.nbChannel)
    
    constructor(width: Int, height: Int, pixelFormat: PixelFormat, source: ByteArray?) : this(width, height, pixelFormat) {
        source?.copyInto(pixels)
    }
    
    constructor(width: Int, height: Int, pixelFormat: PixelFormat, source: ByteBuffer?) : this(width, height, pixelFormat) {
        source?.get(pixels)
    }
    
    override fun get(x: Int, y: Int): Color {
        val start = (y * width + x) * format.nbChannel
        return when (format) {
            PixelFormat.R8 -> Color.from(pixels[start].i)
            PixelFormat.RGB8 -> Color.from(pixels[start].i, pixels[start + 1].i, pixels[start + 2].i)
            PixelFormat.RGBA8 -> Color.from(pixels[start].i, pixels[start + 1].i, pixels[start + 2].i, pixels[start + 3].i)
        }
    }
    
    override fun set(x: Int, y: Int, value: Color) {
        val index = (y * width + x) * format.nbChannel
        pixels[index] = value.rByte
        pixels[index + 1] = value.gByte
        pixels[index + 2] = value.bByte
        pixels[index + 3] = value.aByte
    }
    
    override fun get(index: Int): Byte = pixels[index]
    
    override fun set(index: Int, value: Byte) {
        pixels[index] = value
    }
    
    override fun drawImage(x: Int, y: Int, image: Image) {
        check(image.format == format) { "Image format must be the same" }
        
        val sw = image.width
        val sh = image.height
        
        for (cy in x until x + sw) {
            for (cx in y until y + sh) {
                if (cy < 0 || cy >= width || cx < 0 || cx >= height) continue
                val destI: Int = (cx * width + cy) * format.nbChannel
                val srcI: Int = ((cx - y) * sw + (cy - x)) * image.format.nbChannel
                for (c in 0 until format.nbChannel) {
                    this[destI + c] = image[srcI + c]
                }
            }
        }
    }
    
    override fun fill(color: Color) {
        for (i in 0 until width * height * format.nbChannel step format.nbChannel) {
            for (c in 0 until format.nbChannel) {
                this[i + c] = color.getByte(c)
            }
        }
    }
    
    override fun fill(x: Int, y: Int, width: Int, height: Int, color: Color) {
        for (cy in y until y + height.coerceAtMost(height - y)) {
            for (cx in x until x + width.coerceAtMost(width - x)) {
                this[cx, cy] = color
            }
        }
    }
}