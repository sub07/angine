package dev.sub07.angine.graphics

import dev.sub07.angine.AngineConfiguration
import dev.sub07.angine.event.*
import dev.sub07.angine.math.Vec
import dev.sub07.angine.math.vec
import dev.sub07.angine.memory.Disposable
import dev.sub07.angine.utils.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.system.MemoryStack

class Window : Disposable, KoinComponent {
    
    private val config by inject<AngineConfiguration>()
    
    private val handle: Long
    var title: String = config.appName
        set(value) {
            field = value
            glfwSetWindowTitle(handle, value)
        }
    var width: Int = config.windowWidth
        private set
    var height: Int = config.windowHeight
        private set
    var mouseX = 0
        private set
    var mouseY = 0
        private set
    var windowX = 0
        private set
    var windowY = 0
        private set
    
    val done: Boolean get() = glfwWindowShouldClose(handle)
    var visible: Boolean = false
        set(value) {
            if (value) {
                glfwShowWindow(handle)
            } else {
                glfwHideWindow(handle)
            }
            field = value
        }
    var swapInterval: Int = 0
        set(value) {
            field = value
            glfwSwapInterval(value)
        }
    
    internal var keyCallback: (Key, ActionState, Modifiers) -> Unit = { _, _, _ -> }
        set(value) {
            glfwSetKeyCallback(handle) { _, key, _, action, mods ->
                value(
                    glfwToEngineKey(key), glfwToEngineAction(action), glfwToEngineModifiers(mods)
                )
            }
            field = value
        }
    
    internal var mouseButtonCallback: (MouseButton, ActionState, Modifiers) -> Unit = { _, _, _ -> }
        set(value) {
            glfwSetMouseButtonCallback(handle) { _, button, action, mods ->
                value(
                    glfwToEngineMouseButton(button), glfwToEngineAction(action), glfwToEngineModifiers(mods)
                )
            }
            field = value
        }
    
    
    internal var windowPositionCallback: (Int, Int) -> Unit = { _, _ -> }
        set(value) {
            glfwSetWindowPosCallback(handle) { _, x, y ->
                windowX = x
                windowY = y
                value(x, y)
            }
            field = value
        }
    
    internal var mouseMove: (Int, Int) -> Unit = { _, _ -> }
        set(value) {
            glfwSetCursorPosCallback(handle) { _, x, y ->
                mouseX = x.i
                mouseY = y.i
                value(mouseX, mouseY)
            }
            field = value
        }
    
    internal var mouseScrollCallback: (Int, Int) -> Unit = { _, _ -> }
        set(value) {
            glfwSetScrollCallback(handle) { _, x, y ->
                value(x.toInt(), y.toInt())
            }
            field = value
        }
    
    internal var windowSizeCallback: (Int, Int) -> Unit = { _, _ -> }
        set(value) {
            glfwSetWindowSizeCallback(handle) { _, w, h ->
                width = w.i
                height = h.i
                value(w, h)
            }
            field = value
        }
    
    internal var framebufferSizeCallback: (Int, Int) -> Unit = { _, _ -> }
        set(value) {
            glfwSetFramebufferSizeCallback(handle) { _, w, h ->
                value(w, h)
            }
            field = value
        }
    
    private var previousCursor = 0L
    
    init {
        if (windowCounter == 0) {
            glfwInit()
            GLFWErrorCallback.createPrint().set()
        }
        
        glfwWindowHint(GLFW_RESIZABLE, if (config.isWindowResizable) GLFW_TRUE else GLFW_FALSE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        
        if (config.fullscreen) {
            val monitor = getMonitorFromIndex(config.monitorIndex)
            val mode = glfwGetVideoMode(monitor)!!
            glfwWindowHint(GLFW_RED_BITS, mode.redBits())
            glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits())
            glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits())
            glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate())
            
            handle = if (config.windowWidth == 0 && config.windowHeight == 0) {
                glfwCreateWindow(mode.width(), mode.height(), config.appName, monitor, 0)
            } else {
                glfwCreateWindow(
                    config.windowWidth, config.windowHeight, config.appName, monitor, 0
                )
            }
        } else {
            handle = glfwCreateWindow(
                config.windowWidth, config.windowHeight, config.appName, 0, 0
            )
        }
        
        MemoryStack.stackPush().use {
            val widthPtr = it.malloc()
            val heightPtr = it.malloc()
            glfwGetWindowSize(handle, widthPtr, heightPtr)
            width = widthPtr.value
            height = heightPtr.value
        }
        
        glfwMakeContextCurrent(handle)
        glfwSwapInterval(config.vsync.vSyncInterval)
        
        windowCounter++
    }
    
    override fun dispose() {
        windowCounter--
        glfwDestroyWindow(handle)
        if (previousCursor != 0L) glfwDestroyCursor(previousCursor)
        if (windowCounter == 0) {
            glfwTerminate()
            glfwSetErrorCallback(null)!!.free()
        }
    }
    
    fun setWindowSize(width: Int, height: Int) {
        glfwSetWindowSize(handle, width, height)
        this.width = width
        this.height = height
    }
    
    fun pollEvent() {
        glfwPollEvents()
    }
    
    fun swapBuffers() {
        glfwSwapBuffers(handle)
    }
    
    fun setCursor(img: Image, x: Int, y: Int) {
        if (previousCursor != 0L) glfwDestroyCursor(previousCursor)
        val i = GLFWImage.create()
        i.set(img.width, img.height, img.pixels.toByteBuffer())
        previousCursor = glfwCreateCursor(i, x, y)
        glfwSetCursor(handle, previousCursor)
    }
    
    companion object {
        private fun getMonitorFromIndex(index: Int): Long {
            glfwGetMonitors()!!.also {
                if (index < 0 || index >= it.capacity()) return glfwGetPrimaryMonitor()
                return it[index]
            }
        }
        
        private var windowCounter = 0
    }
}

private val sizeVec = threadLocal(vec())
private val mouseVec = threadLocal(vec())
private val windowPosVec = threadLocal(vec())

val Window.size: Vec get() = sizeVec.get().set(width, height)
val Window.mousePos: Vec get() = mouseVec.get().set(mouseX, mouseY)
val Window.windowPos: Vec get() = windowPosVec.get().set(windowX, windowY)
val Window.aspectRatio: Float get() = height.f / width