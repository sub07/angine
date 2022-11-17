package dev.sub07.angine.ecs.entity

import dev.sub07.angine.ecs.Component
import dev.sub07.angine.ecs.Entity
import dev.sub07.angine.ecs.EntityFilter
import org.koin.core.component.KoinComponent
import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.KClass

class SimpleEntity : Entity, KoinComponent {
    override val id: Long = idCounter.getAndIncrement()
    override var active: Boolean = true
    override val components = mutableListOf<Component>()
    
    @Suppress("UNCHECKED_CAST")
    override fun <C : Component> get(componentType: KClass<C>): C? = components.firstOrNull { it::class == componentType } as? C
    
    override fun <C : Component> add(component: C, replace: Boolean) {
        remove(component::class).let {
            components.add(
                if (it == null) component else {
                    if (replace) component else it
                }
            )
        }
    }
    
    override fun <C : Component> remove(componentType: KClass<C>): C? {
        val index = components.indexOfFirst { it::class == componentType }
        return if (index == -1) {
            null
        } else {
            @Suppress("UNCHECKED_CAST")
            components.removeAt(index) as? C
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SimpleEntity) return false
        
        if (id != other.id) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return id.hashCode()
    }
    
    
    companion object {
        private val idCounter = AtomicLong(0)
    }
}

class SimpleEntityFilter(private val conf: EntityFilter.Conf) : EntityFilter {
    override fun isValid(e: Entity): Boolean {
        val entity = e as SimpleEntity
        val isExcludedInEntity = conf.excluded.any { entity[it] != null }
        val isAllRequiredInEntity = conf.required.all { entity[it] != null }
        return !isExcludedInEntity && isAllRequiredInEntity
    }
}