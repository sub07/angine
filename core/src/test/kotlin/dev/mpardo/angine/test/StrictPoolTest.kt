package dev.sub07.angine.test

import dev.sub07.angine.memory.StrictPool
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs


class StrictPoolTest : TestCase() {
    data class A(var x: Int, var y: Int)
    
    class StrictPoolA : StrictPool<A>({ it.x = 7; it.y = 3 }, 6, 2) {
        override fun create() = A(0, 2)
    }
    
    @Test
    fun `checking that object creation is respected`() {
        val p = StrictPoolA()
        
        val pooled = p.pool()
        assertIs<A>(pooled)
    }
    
    @Test
    fun `checking that object reset is respected`() {
        val p = StrictPoolA()
        
        var pooled = p.pool()
        assertEquals(7, pooled.x)
        assertEquals(3, pooled.y)
        
        pooled = p.pool()
        assertEquals(7, pooled.x)
        assertEquals(3, pooled.y)
    }
    
    @Test
    fun `checking that max size is respected`() {
        val p = StrictPoolA()
        
        repeat(6) {
            p.pool()
        }
        
        assertFails {
            p.pool()
        }
    }
    
    @Test
    fun `checking that we can't give too much object`() {
        val p1 = StrictPoolA()
        
        assertFails {
            p1.pool(A(5, 6))
        }
        
        val p2 = StrictPoolA()
        
        val pooled = p2.pool()
        
        assertFails {
            p2.pool(pooled)
            p2.pool(pooled)
        }
    }
}