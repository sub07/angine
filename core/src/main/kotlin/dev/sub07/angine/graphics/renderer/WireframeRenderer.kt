package dev.sub07.angine.graphics.renderer

import dev.sub07.angine.AngineConfiguration
import dev.sub07.angine.graphics.Color
import dev.sub07.angine.graphics.ZIndex
import dev.sub07.angine.graphics.primitive.DrawPrimitive
import dev.sub07.angine.graphics.primitive.Graphics
import dev.sub07.angine.graphics.primitive.Shader
import dev.sub07.angine.graphics.primitive.bind
import dev.sub07.angine.math.PolygonUtils
import dev.sub07.angine.math.Vec
import dev.sub07.angine.memory.Floats
import org.koin.core.component.KoinComponent

class WireframeRenderer(config: AngineConfiguration, private val graphics: Graphics, shader: Shader = graphics.makeShader(vSources, fSources)) :
    BatchedRenderer(config.batchSize, components, shader, graphics), KoinComponent {
    
    override fun flushImpl() {
        shader.bind()
        graphics.draw(DrawPrimitive.Lines, nbVertices, false)
    }
    
    fun polygon(vertices: Floats, color: Color = Color.White) {
        check()
        val lines = PolygonUtils.convertPointListToLines(vertices, false)
        for (i in lines.indices step 4) {
            line(lines[i], lines[i + 1], lines[i + 2], lines[i + 3], color)
        }
    }
    
    fun polyline(vertices: Floats, color: Color = Color.White) {
        check()
        val lines = PolygonUtils.convertPointListToLines(vertices, true)
        for (i in lines.indices step 4) {
            line(lines[i], lines[i + 1], lines[i + 2], lines[i + 3], color)
        }
    }
    
    fun line(x1: Float, y1: Float, x2: Float, y2: Float, color: Color = Color.White) {
        check()
        addVertex(x1, y1, ZIndex.ratio, color.r, color.g, color.b, color.a)
        addVertex(x2, y2, ZIndex.ratio, color.r, color.g, color.b, color.a)
    }
    
    companion object {
        val components = intArrayOf(3, 4)
        
        val vSources = commonVertexShader
        
        val fSources = """
            #version 460
            
            in vec4 tint;
            layout(location = 0) out vec4 finalPixelColor;
            
            void main() {
                finalPixelColor = tint;
                if (finalPixelColor.a == 0.0) discard;
            }
            """.trimIndent()
    }
}

fun WireframeRenderer.line(v1: Vec, v2: Vec, color: Color = Color.White) {
    line(v1.xc, v1.yc, v2.xc, v2.yc, color)
}