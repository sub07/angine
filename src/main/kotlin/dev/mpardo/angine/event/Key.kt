package dev.mpardo.angine.event

import org.lwjgl.glfw.GLFW

enum class Key {
    Unknown,
    Escape,
    F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12,
    PrintScreen, ScrollLock, Pause,
    Insert, Home, PageUp, PageDown, End, Delete,
    Up, Down, Left, Right,
    NumLock, NpDivide, NpMultiply, NpMinus, NpPlus, NpEnter, NpDot,
    Grave,
    Np0, Np1, Np2, Np3, Np4, Np5, Np6, Np7, Np8, Np9,
    Kb0, Kb1, Kb2, Kb3, Kb4, Kb5, Kb6, Kb7, Kb8, Kb9, Minus, Equals,
    RightBracket, LeftBracket,
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
    Comma, Dot, Semicolon, Slash, Backslash, Apostrophe,
    Tab, CapsLock, LeftShift, LeftControl,
    Windows, LeftAlt, Space, RightAlt, RightControl,
    RightShift, Enter, Backspace,
}

class KeyboardState {
    internal val pressed = BooleanArray(Key.values().size) { false }
    
    operator fun get(key: Key) = pressed[key.ordinal]
    
    internal operator fun set(key: Key, state: Boolean) {
        pressed[key.ordinal] = state
    }
}

data class Modifiers(
    val ctrl: Boolean = false,
    val alt: Boolean = false,
    val shift: Boolean = false,
    val windows: Boolean = false,
    val numLock: Boolean = false,
    val capsLock: Boolean = false
)

internal fun glfwToEngineModifiers(mods: Int) = Modifiers(
    (mods and GLFW.GLFW_MOD_CONTROL) != 0,
    (mods and GLFW.GLFW_MOD_ALT) != 0,
    (mods and GLFW.GLFW_MOD_SHIFT) != 0,
    (mods and GLFW.GLFW_MOD_SUPER) != 0,
    (mods and GLFW.GLFW_MOD_NUM_LOCK) != 0,
    (mods and GLFW.GLFW_MOD_CAPS_LOCK) != 0
)

internal fun glfwToEngineKey(key: Int) = when (key) {
    GLFW.GLFW_KEY_A -> Key.A
    GLFW.GLFW_KEY_B -> Key.B
    GLFW.GLFW_KEY_C -> Key.C
    GLFW.GLFW_KEY_D -> Key.D
    GLFW.GLFW_KEY_E -> Key.E
    GLFW.GLFW_KEY_F -> Key.F
    GLFW.GLFW_KEY_G -> Key.G
    GLFW.GLFW_KEY_H -> Key.H
    GLFW.GLFW_KEY_I -> Key.I
    GLFW.GLFW_KEY_J -> Key.J
    GLFW.GLFW_KEY_K -> Key.K
    GLFW.GLFW_KEY_L -> Key.L
    GLFW.GLFW_KEY_M -> Key.M
    GLFW.GLFW_KEY_N -> Key.N
    GLFW.GLFW_KEY_O -> Key.O
    GLFW.GLFW_KEY_P -> Key.P
    GLFW.GLFW_KEY_Q -> Key.Q
    GLFW.GLFW_KEY_R -> Key.R
    GLFW.GLFW_KEY_S -> Key.S
    GLFW.GLFW_KEY_T -> Key.T
    GLFW.GLFW_KEY_U -> Key.U
    GLFW.GLFW_KEY_V -> Key.V
    GLFW.GLFW_KEY_W -> Key.W
    GLFW.GLFW_KEY_X -> Key.X
    GLFW.GLFW_KEY_Y -> Key.Y
    GLFW.GLFW_KEY_Z -> Key.Z
    GLFW.GLFW_KEY_1 -> Key.Kb1
    GLFW.GLFW_KEY_2 -> Key.Kb2
    GLFW.GLFW_KEY_3 -> Key.Kb3
    GLFW.GLFW_KEY_4 -> Key.Kb4
    GLFW.GLFW_KEY_5 -> Key.Kb5
    GLFW.GLFW_KEY_6 -> Key.Kb6
    GLFW.GLFW_KEY_7 -> Key.Kb7
    GLFW.GLFW_KEY_8 -> Key.Kb8
    GLFW.GLFW_KEY_9 -> Key.Kb9
    GLFW.GLFW_KEY_0 -> Key.Kb0
    GLFW.GLFW_KEY_ENTER -> Key.Enter
    GLFW.GLFW_KEY_ESCAPE -> Key.Escape
    GLFW.GLFW_KEY_BACKSPACE -> Key.Backspace
    GLFW.GLFW_KEY_TAB -> Key.Tab
    GLFW.GLFW_KEY_SPACE -> Key.Space
    GLFW.GLFW_KEY_MINUS -> Key.Minus
    GLFW.GLFW_KEY_EQUAL -> Key.Equals
    GLFW.GLFW_KEY_LEFT_BRACKET -> Key.LeftBracket
    GLFW.GLFW_KEY_RIGHT_BRACKET -> Key.RightBracket
    GLFW.GLFW_KEY_BACKSLASH -> Key.Backslash
    GLFW.GLFW_KEY_SEMICOLON -> Key.Semicolon
    GLFW.GLFW_KEY_APOSTROPHE -> Key.Apostrophe
    GLFW.GLFW_KEY_GRAVE_ACCENT -> Key.Grave
    GLFW.GLFW_KEY_COMMA -> Key.Comma
    GLFW.GLFW_KEY_PERIOD -> Key.Dot
    GLFW.GLFW_KEY_SLASH -> Key.Slash
    GLFW.GLFW_KEY_CAPS_LOCK -> Key.CapsLock
    GLFW.GLFW_KEY_F1 -> Key.F1
    GLFW.GLFW_KEY_F2 -> Key.F2
    GLFW.GLFW_KEY_F3 -> Key.F3
    GLFW.GLFW_KEY_F4 -> Key.F4
    GLFW.GLFW_KEY_F5 -> Key.F5
    GLFW.GLFW_KEY_F6 -> Key.F6
    GLFW.GLFW_KEY_F7 -> Key.F7
    GLFW.GLFW_KEY_F8 -> Key.F8
    GLFW.GLFW_KEY_F9 -> Key.F9
    GLFW.GLFW_KEY_F10 -> Key.F10
    GLFW.GLFW_KEY_F11 -> Key.F11
    GLFW.GLFW_KEY_F12 -> Key.F12
    GLFW.GLFW_KEY_PRINT_SCREEN -> Key.PrintScreen
    GLFW.GLFW_KEY_SCROLL_LOCK -> Key.ScrollLock
    GLFW.GLFW_KEY_PAUSE -> Key.Pause
    GLFW.GLFW_KEY_INSERT -> Key.Insert
    GLFW.GLFW_KEY_HOME -> Key.Home
    GLFW.GLFW_KEY_PAGE_UP -> Key.PageUp
    GLFW.GLFW_KEY_DELETE -> Key.Delete
    GLFW.GLFW_KEY_END -> Key.End
    GLFW.GLFW_KEY_PAGE_DOWN -> Key.PageDown
    GLFW.GLFW_KEY_RIGHT -> Key.Right
    GLFW.GLFW_KEY_LEFT -> Key.Left
    GLFW.GLFW_KEY_DOWN -> Key.Down
    GLFW.GLFW_KEY_UP -> Key.Up
    GLFW.GLFW_KEY_NUM_LOCK -> Key.NumLock
    GLFW.GLFW_KEY_KP_DIVIDE -> Key.NpDivide
    GLFW.GLFW_KEY_KP_MULTIPLY -> Key.NpMultiply
    GLFW.GLFW_KEY_KP_SUBTRACT -> Key.NpMinus
    GLFW.GLFW_KEY_KP_ADD -> Key.NpPlus
    GLFW.GLFW_KEY_KP_ENTER -> Key.NpEnter
    GLFW.GLFW_KEY_KP_1 -> Key.Np1
    GLFW.GLFW_KEY_KP_2 -> Key.Np2
    GLFW.GLFW_KEY_KP_3 -> Key.Np3
    GLFW.GLFW_KEY_KP_4 -> Key.Np4
    GLFW.GLFW_KEY_KP_5 -> Key.Np5
    GLFW.GLFW_KEY_KP_6 -> Key.Np6
    GLFW.GLFW_KEY_KP_7 -> Key.Np7
    GLFW.GLFW_KEY_KP_8 -> Key.Np8
    GLFW.GLFW_KEY_KP_9 -> Key.Np9
    GLFW.GLFW_KEY_KP_0 -> Key.Np0
    GLFW.GLFW_KEY_KP_DECIMAL -> Key.NpDot
    GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER -> Key.Windows
    GLFW.GLFW_KEY_LEFT_SHIFT -> Key.LeftShift
    GLFW.GLFW_KEY_LEFT_CONTROL -> Key.LeftControl
    GLFW.GLFW_KEY_LEFT_ALT -> Key.LeftAlt
    GLFW.GLFW_KEY_RIGHT_ALT -> Key.RightAlt
    GLFW.GLFW_KEY_RIGHT_CONTROL -> Key.RightControl
    GLFW.GLFW_KEY_RIGHT_SHIFT -> Key.RightShift
    else -> Key.Unknown
}