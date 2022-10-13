package dev.mpardo.angine.test.ecs

import dev.mpardo.angine.ecs.Component
import dev.mpardo.angine.ecs.Entity
import dev.mpardo.angine.ecs.System
import dev.mpardo.angine.ecs.entityFilter
import dev.mpardo.angine.test.TestCase
import org.koin.core.component.get
import kotlin.test.Test
import kotlin.test.assertEquals

class SystemTest : TestCase() {
    
    class StringComponent(var name: String) : Component
    
    class StringTestSystem : System(entityFilter { required(StringComponent::class) }) {
        override fun doUpdate(entities: List<Entity>) {
            entities.forEach {
                val stringComponent = it[StringComponent::class]!!
                stringComponent.name += "45"
            }
        }
    }
    
    @Test
    fun `test string test system`() {
        val printSystem = StringTestSystem()
        val e1 = get<Entity>()
        e1.add(StringComponent("test"))
        printSystem.update(setOf(e1))
        assertEquals("test45", e1[StringComponent::class]?.name)
        printSystem.update(setOf(e1))
        assertEquals("test4545", e1[StringComponent::class]?.name)
    }
}