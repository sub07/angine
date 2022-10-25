package dev.mpardo.angine.event

interface ResizeEventListener {
    fun onResize(width: Int, height: Int)
}

object ResizeEventManager {
    private val listeners = mutableListOf<ResizeEventListener>()
    
    fun resize(w: Int, h: Int) {
        for (listener in listeners) {
            listener.onResize(w, h)
        }
    }
    
    fun add(listener: ResizeEventListener) { listeners += listener }
    
    operator fun plusAssign(listener: ResizeEventListener) { add(listener) }
}