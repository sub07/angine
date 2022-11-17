package dev.sub07.angine.scene

import dev.sub07.angine.ecs.Component
import dev.sub07.angine.ecs.World
import org.koin.core.component.KoinComponent

class SceneBuilder {
    val scene = Scene()
    val world get() = scene.world
    
    class EntityBuilder(world: World) : KoinComponent {
        val entity = world.createEntity()
        fun component(componentBuilder: () -> Component) {
            entity += componentBuilder()
        }
    }
    
    fun entity(builder: EntityBuilder.() -> Unit) {
        val entityBuilder = EntityBuilder(world)
        entityBuilder.builder()
        world += entityBuilder.entity
    }
    
}

fun scene(builder: SceneBuilder.() -> Unit): Scene {
    val sceneBuilder = SceneBuilder()
    sceneBuilder.builder()
    return sceneBuilder.scene
}