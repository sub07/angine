package dev.sub07.angine.playground

import dev.sub07.angine.Angine
import dev.sub07.angine.AngineConfiguration
import dev.sub07.angine.ecs.*
import dev.sub07.angine.ecs.entity.SimpleEntity
import dev.sub07.angine.ecs.entity.SimpleEntityFilter
import dev.sub07.angine.graphics.Window
import dev.sub07.angine.graphics.primitive.GLGraphics
import dev.sub07.angine.graphics.primitive.Graphics
import dev.sub07.angine.graphics.renderer.WireframeRenderer
import dev.sub07.angine.graphics.renderer.use
import dev.sub07.angine.math.MutableVec
import dev.sub07.angine.math.VertexTransformer
import dev.sub07.angine.math.vec
import dev.sub07.angine.memory.MutableFloats
import dev.sub07.angine.memory.StrictVecPool
import dev.sub07.angine.memory.VecPool
import dev.sub07.angine.memory.floats
import dev.sub07.angine.scene.scene
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
import kotlin.time.Duration

val angineModule = module {
    single { Angine() }
    single { Window() }
    single { WireframeRenderer(get(), get()) }
    single<Graphics> { GLGraphics() }
}

val configurationModule = module {
    single { AngineConfiguration() }
    single {
        scene {
            world += ShapeSystem()
            entity {
                component { ShapeComponent(floats(0f, 0f, 100f, 150f, 300f, 300f, 10f, 400f), isWireframe = true) }
                component { TransformComponent(translation = vec(0f, 0f)) }
            }
        }
    }
    single<VecPool> { StrictVecPool() }
}

val escModule = module {
    factory<Entity> { SimpleEntity() }
    factory<EntityFilter> { param -> SimpleEntityFilter(param.get()) }
}

data class ShapeComponent(
    var vertices: MutableFloats,
    var isWireframe: Boolean = false,
    var isClosedShape: Boolean = true,
) : Component

data class TransformComponent(
    val translation: MutableVec = vec(0),
    val scale: MutableVec = vec(1),
    var rotation: Float = 0f,
    val origin: MutableVec = vec(0)
) : Component {
    fun intoVertexTransformer() = VertexTransformer(translation, scale, rotation, origin)
}

class ShapeSystem : System(entityFilter { required(ShapeComponent::class) }), KoinComponent {
    private val wireframeRenderer by inject<WireframeRenderer>()
    override fun doUpdate(entities: List<Entity>, dt: Duration) {
        wireframeRenderer.use {
            entities.forEach { entity ->
                val shapeComponent = entity[ShapeComponent::class]!!
                val vertexTransformer = entity[TransformComponent::class]?.intoVertexTransformer()
                val transformedVertex =
                    vertexTransformer?.transformed(shapeComponent.vertices) ?: shapeComponent.vertices

                if (shapeComponent.isWireframe) {
                    if (shapeComponent.isClosedShape) {
                        wireframeRenderer.polygon(transformedVertex)
                    } else {
                        wireframeRenderer.polyline(transformedVertex)
                    }
                } else {
                    println("Non wireframe not supported yet")
                }
            }
        }
    }
}


fun main() {
    Angine.launch(angineModule, configurationModule, escModule)
}