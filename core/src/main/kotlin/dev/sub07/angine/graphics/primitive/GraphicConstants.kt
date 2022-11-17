package dev.sub07.angine.graphics.primitive

import org.lwjgl.opengl.GL46.*

enum class DrawPrimitive(val openglValue: Int) {
    TriangleStrip(GL_TRIANGLE_STRIP),
    TriangleFan(GL_TRIANGLE_FAN),
    LineStrip(GL_LINE_STRIP),
    Lines(GL_LINES),
    Triangles(GL_TRIANGLES),
    LineLoop(GL_LINE_LOOP)
}

enum class BufferType(val openglValue: Int) {
    Array(GL_ARRAY_BUFFER),
    Index(GL_ELEMENT_ARRAY_BUFFER)
}

enum class BufferUsage(val openglValue: Int) {
    Dynamic(GL_DYNAMIC_DRAW),
    Static(GL_STATIC_DRAW)
}

enum class TextureFilter(val openglValue: Int) {
    Nearest(GL_NEAREST),
    Linear(GL_LINEAR)
}

enum class TextureWrap(val openglValue: Int) {
    Repeat(GL_REPEAT),
    MirroredRepeat(GL_MIRRORED_REPEAT),
    ClampToEdge(GL_CLAMP_TO_EDGE),
    ClampToBorder(GL_CLAMP_TO_BORDER)
}