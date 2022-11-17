package dev.sub07.angine.ecs

import kotlin.time.Duration

abstract class System(private val entityFilter: EntityFilter) {
    fun update(entities: Set<Entity>, dt: Duration) {
        doUpdate(entityFilter.get(entities.filter { it.active }), dt)
    }
    
    protected abstract fun doUpdate(entities: List<Entity>, dt: Duration)
}