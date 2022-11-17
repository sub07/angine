package dev.sub07.angine.test.ecs

import dev.sub07.angine.ecs.*
import dev.sub07.angine.test.TestCase
import org.koin.core.component.get
import kotlin.test.Test
import kotlin.test.assertEquals

class WorldTest : TestCase() {
    
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
    fun `test world with string test system`() {
        val world = World()
        
        world += StringTestSystem()
        
        val e1 = get<Entity>()
        e1.add(StringComponent("test"))
        
        world += e1
        
        world.step()
        assertEquals("test45", e1[StringComponent::class]?.name)
        world.step()
        assertEquals("test4545", e1[StringComponent::class]?.name)
    }
}