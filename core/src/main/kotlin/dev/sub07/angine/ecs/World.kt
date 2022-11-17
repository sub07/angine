package dev.sub07.angine.ecs

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.time.Duration

class World : KoinComponent {
    private val systems = mutableSetOf<System>()
    private val entities = mutableSetOf<Entity>()
    
    fun step(dt: Duration) {
        systems.forEach {
            it.update(entities, dt)
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
    
    operator fun plusAssign(system: System) {
        systems.add(system)
    }
    
    operator fun plusAssign(e: Entity) {
        entities.add(e)
    }
}