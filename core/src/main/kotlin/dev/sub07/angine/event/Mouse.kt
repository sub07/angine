package dev.sub07.angine.event

import org.lwjgl.glfw.GLFW

enum class MouseButton {
    Left, Right, Middle, Unknown
}

enum class ActionState {
    Pressed, Released, Repeat, Unknown
}

class MouseState {
    internal val pressed = BooleanArray(MouseButton.values().size) { false }
    
    operator fun get(button: MouseButton) = pressed[button.ordinal]
    
    internal operator fun set(button: MouseButton, state: Boolean) {
        pressed[button.ordinal] = state
    }
}

internal fun glfwToEngineMouseButton(mouseButton: Int) = when (mouseButton) {
    GLFW.GLFW_MOUSE_BUTTON_RIGHT -> MouseButton.Right
    GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> MouseButton.Middle
    GLFW.GLFW_MOUSE_BUTTON_LEFT -> MouseButton.Left
    else -> MouseButton.Unknown
}

internal fun glfwToEngineAction(action: Int) = when (action) {
    GLFW.GLFW_PRESS -> ActionState.Pressed
    GLFW.GLFW_RELEASE -> ActionState.Released
    GLFW.GLFW_REPEAT -> ActionState.Repeat
    else -> ActionState.Unknown
}