package dev.sub07.angine

import dev.sub07.angine.event.*
import dev.sub07.angine.graphics.Window
import dev.sub07.angine.graphics.primitive.Graphics
import dev.sub07.angine.graphics.renderer.WireframeRenderer
import dev.sub07.angine.scene.Scene
import dev.sub07.angine.utils.now
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.core.module.Module

data class AngineConfiguration(
    val windowWidth: Int = 800,
    val windowHeight: Int = 600,
    val appName: String = "My App",
    val isWindowResizable: Boolean = true,
    val vsync: VSync = VSync.Enabled,
    val fullscreen: Boolean = false,
    val monitorIndex: Int = 0,
    val batchSize: Int = 8000,
    val drawWhileWindowResize: Boolean = false,
    val fpsFrameSpread: Int = 50
) {
    enum class VSync(val vSyncInterval: Int) {
        Disabled(0), Enabled(1), Div2(2), Div4(3),
    }
}

class Angine : KoinComponent {
    val window = get<Window>()
    val graphics by inject<Graphics>()
    
    var done: Boolean = false
        get() = window.done || field
    val keyboardState = KeyboardState()
    val mouseState = MouseState()
    private var modifiers = Modifiers()
    val resizeEventListeners = mutableListOf<ResizeEventListener>()
    
    init {
        window.keyCallback = ::keyCallback
        window.mouseButtonCallback = ::mouseCallback
        window.mouseMove = ::mouseMoveCallback
        window.mouseScrollCallback = ::mouseScrollCallback
        window.framebufferSizeCallback = ::framebufferCallback
        window.windowSizeCallback = ::windowResizeCallback
        window.windowPositionCallback = ::windowMoveCallback
        graphics.initialize(false)
    }
    
    private fun run() {
        window.visible = true
        
        windowResizeCallback(window.width, window.height)
        
        val initialScene = get<Scene>()
        
        var last = now
        while (!done) {
            val dt = now - last
            last = now
            graphics.clear()
            window.pollEvent()
            initialScene.world.step(dt)
            window.swapBuffers()
        }
        
        window.visible = false
    }
    
    @Suppress("UNUSED_PARAMETER")
    private fun windowMoveCallback(w: Int, h: Int) {
    }
    
    private fun framebufferCallback(w: Int, h: Int) {
        graphics.setViewport(0, 0, w, h)
    }
    
    private fun windowResizeCallback(w: Int, h: Int) {
        if (w == 0 || h == 0) return
        resizeEventListeners.forEach { it.onResize(w, h) }
    }
    
    private fun mouseScrollCallback(x: Int, y: Int) {
    
    }
    
    private fun mouseMoveCallback(x: Int, y: Int) {
    
    }
    
    private fun mouseCallback(mouseButton: MouseButton, actionState: ActionState, modifiers: Modifiers) {
        this.modifiers = modifiers
        when (actionState) {
            ActionState.Pressed -> {
                mouseState[mouseButton] = true
            }
            
            ActionState.Released -> {
                mouseState[mouseButton] = false
            }
            
            else -> {
            }
        }
    }
    
    private fun keyCallback(key: Key, actionState: ActionState, modifiers: Modifiers) {
        this.modifiers = modifiers
        when (actionState) {
            ActionState.Pressed -> {
                keyboardState[key] = true
            }
            
            ActionState.Released -> {
                keyboardState[key] = false
            }
            
            else -> {
            }
        }
    }
    
    fun exit() {
        done = true
    }
    
    companion object : KoinComponent {
        fun launch(vararg userModules: Module) {
            val koinInstance = startKoin {
                logger(PrintLogger(Level.ERROR))
                modules(*userModules)
            }
            
            val angine = get<Angine>()
            angine.resizeEventListeners += get<WireframeRenderer>()
            angine.run()
            get<WireframeRenderer>().dispose()
            get<Window>().dispose()
            
            koinInstance.close()
        }
    }
}