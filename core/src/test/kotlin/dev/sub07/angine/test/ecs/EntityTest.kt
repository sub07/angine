package dev.sub07.angine.test.ecs

import dev.sub07.angine.ecs.Component
import dev.sub07.angine.ecs.Entity
import dev.sub07.angine.ecs.entityFilter
import dev.sub07.angine.test.TestCase
import org.koin.core.component.get
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EntityTest : TestCase() {

    class TestComponent(val data: String) : Component
    class Test2Component(val data: Int) : Component
    class Test3Component(val data: Int) : Component
    class Test4Component(val data: Int) : Component
    class Test5Component(val data: Int) : Component

    @Test
    fun `test that entity has no component at creation`() {
        val e = get<Entity>()
        assertEquals(0, e.components.size)
        assertEquals(true, e.active)
    }

    @Test
    fun `test that entity has components when adding some`() {
        val e = get<Entity>()

        e += TestComponent("test")
        assertEquals(1, e.components.size)
        val testComp = e[TestComponent::class]
        assertNotNull(testComp)
        assertEquals("test", testComp.data)
        e.add(Test2Component(5))
        assertEquals(2, e.components.size)
        val test2Comp = e[Test2Component::class]
        assertNotNull(test2Comp)
        assertEquals(5, test2Comp.data)
        e.remove(TestComponent::class)
        assertEquals(1, e.components.size)
        assertNull(e[TestComponent::class])
        e -= Test2Component::class
        assertEquals(0, e.components.size)
        assertNull(e[Test2Component::class])
    }

    @Test
    fun `test that multiples entities have different ids`() {
        val ids = (0..50000).map { get<Entity>().id }
        assertEquals(ids.size, ids.distinct().size)
    }

    private fun createEntityWithComponents(vararg c: Component) = get<Entity>().apply {
        for (component in c) {
            this += component
        }
    }

    @Test
    fun `test entity filter with no requirement`() {
        val filter = entityFilter()

        val entities = setOf(
            createEntityWithComponents(),
            createEntityWithComponents(),
            createEntityWithComponents(),
            createEntityWithComponents(),
            createEntityWithComponents(),
        )

        val retrieved = filter.get(entities).size
        assertEquals(entities.size, retrieved)
    }

    @Test
    fun `test entity retrieval with one requirement`() {
        val filter = entityFilter {
            required(TestComponent::class)
        }

        val entities = setOf(
            createEntityWithComponents(TestComponent("test1"), Test4Component(8)),
            createEntityWithComponents(Test4Component(8)),
            createEntityWithComponents(TestComponent("test2"), Test5Component(8)),
            createEntityWithComponents(TestComponent("test3"), Test3Component(8)),
            createEntityWithComponents(),
            createEntityWithComponents(),
        )

        val retrieved = filter.get(entities)

        assertEquals(3, retrieved.size)

        val (e1, e2, e3) = retrieved

        assertEquals("test1", e1[TestComponent::class]?.data)
        assertEquals("test2", e2[TestComponent::class]?.data)
        assertEquals("test3", e3[TestComponent::class]?.data)
    }

    @Test
    fun `test entity retrieval with one requirement and one exclusion`() {
        val filter = entityFilter {
            required(TestComponent::class)
            exclude(Test5Component::class)
        }

        val entities = setOf(
            createEntityWithComponents(TestComponent("test1"), Test4Component(8)),
            createEntityWithComponents(Test4Component(8)),
            createEntityWithComponents(TestComponent("test2"), Test5Component(8)),
            createEntityWithComponents(TestComponent("test3"), Test3Component(8)),
            createEntityWithComponents(),
            createEntityWithComponents(),
        )

        val retrieved = filter.get(entities)

        assertEquals(2, retrieved.size)

        val (e1, e2) = retrieved

        assertEquals("test1", e1[TestComponent::class]?.data)
        assertEquals("test3", e2[TestComponent::class]?.data)
    }
}