package dev.sub07.angine.test.ecs

import dev.sub07.angine.ecs.Component
import dev.sub07.angine.ecs.Entity
import dev.sub07.angine.ecs.System
import dev.sub07.angine.ecs.entityFilter
import dev.sub07.angine.test.TestCase
import org.koin.core.component.get
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SystemTest : TestCase() {
    
    class StringComponent(var name: String) : Component
    
    class StringTestSystem : System(entityFilter { required(StringComponent::class) }) {
        override fun doUpdate(entities: List<Entity>, dt: Duration) {
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
        printSystem.update(setOf(e1), 0.seconds)
        assertEquals("test45", e1[StringComponent::class]?.name)
        printSystem.update(setOf(e1), 0.seconds)
        assertEquals("test4545", e1[StringComponent::class]?.name)
    }
}