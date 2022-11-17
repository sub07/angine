package dev.sub07.angine.ecs

import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class World : KoinComponent {
    private val systems = mutableSetOf<System>()
    private val entities = mutableSetOf<Entity>()
    
    fun step() {
        systems.forEach {
            it.update(entities)
        }
    }
    
    fun createEntity() = get<Entity>().also {
        entities.add(it)
    }
    
    fun removeEntity(e: Entity) {
        entities.remove(e)
    }
    
    fun addSystem(vararg s: System) {
        systems += s
    }
    
    fun removeSystem(s: System) {
        systems.remove(s)
    }
    
    operator fun plusAssign(something: System) {
        systems.add(something)
    }
    
    operator fun plusAssign(something: Entity) {
        entities.add(something)
    }
}