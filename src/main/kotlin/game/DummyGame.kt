package game


import engine.IGameLogic
import engine.Window
import kotlin.Throws
import org.lwjgl.glfw.GLFW
import java.lang.Exception

class DummyGame : IGameLogic {
    private var direction = 0
    private var color = 0.0f
    private lateinit var renderer: Renderer

    @Throws(Exception::class)
    override fun init() {
        renderer = Renderer()
        renderer.init()
    }

    override fun input(window: Window?) {
        if (window != null) {
            direction = when {
                window.isKeyPressed(GLFW.GLFW_KEY_UP) -> {
                    1
                }
                window.isKeyPressed(GLFW.GLFW_KEY_DOWN) -> {
                    -1
                }
                else -> {
                    0
                }
            }
        }
    }

    override fun update(interval: Float) {
        color += direction * 0.01f
        if (color > 1) {
            color = 1.0f
        } else if (color < 0) {
            color = 0.0f
        }
    }

    override fun render(window: Window?) {
        if (window != null) {
            window.setClearColor(color, color, color, 0.0f)
            renderer.render(window)
        }}

    override fun cleanup() {
        renderer.cleanup()
    }
}