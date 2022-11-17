package dev.sub07.angine.ecs

abstract class System(private val entityFilter: EntityFilter) {
    fun update(entities: Set<Entity>) {
        doUpdate(entityFilter.get(entities.filter { it.active }))
    }
    
    protected abstract fun doUpdate(entities: List<Entity>)
}