package dev.sub07.angine.ecs

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

class Builder : KoinComponent {
    private val required = mutableListOf<KClass<out Component>>()
    private val excluded = mutableListOf<KClass<out Component>>()
    fun required(vararg componentType: KClass<out Component>) {
        required += componentType
    }

    fun exclude(vararg componentType: KClass<out Component>) {
        excluded += componentType
    }

    fun get() = get<EntityFilter> { parametersOf(EntityFilter.Conf(required, excluded)) }
}

fun entityFilter(conf: Builder.() -> Unit = {}): EntityFilter = Builder().apply(conf).get()

interface EntityFilter {
    data class Conf(val required: List<KClass<out Component>>, val excluded: List<KClass<out Component>>)

    fun isValid(e: Entity): Boolean
    fun get(entities: Iterable<Entity>) = entities.filter(this::isValid)
}

interface Entity {
    val id: Long
    var active: Boolean
    val components: Collection<Component>
    operator fun <C : Component> get(componentType: KClass<C>): C?
    fun <C : Component> add(component: C, replace: Boolean = true)
    fun <C : Component> remove(componentType: KClass<C>): C?
    operator fun <C : Component> plusAssign(component: C) {
        add(component, true)
    }

    operator fun <C : Component> minusAssign(component: KClass<C>) {
        remove(component)
    }
}
