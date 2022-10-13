package dev.mpardo.angine.memory

interface Pool<T> {
    fun pool(): T
    fun pool(obj: T)
}

inline fun <T, R> Pool<T>.borrow(block: (T) -> R): R {
    val obj = pool()
    val res = block(obj)
    pool(obj)
    return res
}

/**
 * A very simple pool implementation with no overhead.
 */

abstract class SimplePool<T>(initialSize: Int) : Pool<T> {
    private val pool = MutableList(initialSize) { create() }
    
    override fun pool() = if (pool.isEmpty()) create() else pool.removeAt(pool.size - 1)
    
    override fun pool(obj: T) {
        pool.add(obj)
    }
    
    protected abstract fun create(): T
}

/**
 * A complex pool runs many checks to ensure everything is ok.
 * Useful for development, but not for production.
 */
abstract class StrictPool<T>(
    private val resetObj: (obj: T) -> Unit, private val maxPoolSize: Int = 20, initialPoolSize: Int = maxPoolSize / 2
) : Pool<T> {
    private val created = MutableList(initialPoolSize) { create() }
    private val current = created.toMutableList()
    
    init {
        println("StrictPool used, please use simple pool for production.")
        check(initialPoolSize <= maxPoolSize) { "The initial size is bigger than the max size" }
        check(maxPoolSize > 0) { "The maximum pool size must be greater than 0" }
    }
    
    override fun pool(): T {
        if (current.isEmpty() && created.size < maxPoolSize) {
            create().also {
                created.add(it)
                current.add(it)
            }
        }
        check(current.isNotEmpty()) { "This pool is not allowed to create more than $maxPoolSize object(s)" }
        return current.removeFirst().also(resetObj)
    }
    
    override fun pool(obj: T) {
        check(obj in created) { "This object doesn't belong to this pool" }
        check(obj !in current) {
            "This pool already contains this object, make sure you don't give it back twice"
        }
        current.add(obj)
    }
    
    protected abstract fun create(): T
}