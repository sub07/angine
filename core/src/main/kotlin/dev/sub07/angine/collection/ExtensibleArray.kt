package dev.sub07.angine.collection

class ExtensibleArray<T>(initialSize: Int = 10) {
    private var data: Array<Any?> = Array(initialSize) { null }
    
    private fun ensure(size: Int) {
        if (size < data.size) return
        data = data.copyOf(size)
    }
    
    operator fun get(index: Int): T? {
        ensure(index)
        @Suppress("UNCHECKED_CAST")
        return data[index] as? T
    }
    
    operator fun set(index: Int, elem: T) {
        ensure(index)
        data[index] = elem
    }
    
    fun removeAt(index: Int) {
        ensure(index)
        data[index] = null
    }
    
    fun clear() {
        for (i in data.indices)
            data[i] = null
    }
}
