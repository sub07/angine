package dev.mpardo.angine

enum class VSync(val vSyncInterval: Int) {
    Disabled(0), Enabled(1), Div2(2), Div4(3),
}

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
)