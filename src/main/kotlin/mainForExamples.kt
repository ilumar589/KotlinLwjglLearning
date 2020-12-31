import engine.SHADERS_BASE_PATH
import engine.ShaderProgram
import engine.readFileAsText
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil.NULL

var windowHandle = NULL

fun init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.

    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set()

    check(GLFW.glfwInit()) { "Illegal state exception" }

    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE) // the window will stay hidden after creation

    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE) // the window will be resizable


    val width = 800
    val height = 600

    windowHandle = GLFW.glfwCreateWindow(width, height, "LearnOpenGL", NULL, NULL)

    if (windowHandle == NULL) {
        println("Failed to create GLFW window")
        GLFW.glfwTerminate()
    }

    GLFW.glfwSetKeyCallback(
        windowHandle
    ) { window: Long, key: Int, scanCode: Int, action: Int, mods: Int ->
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
            GLFW.glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
        }
    }

    // Get the resolution of the primary monitor

    // Get the resolution of the primary monitor
    val videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
    // Center our window

    // Center our window
    GLFW.glfwSetWindowPos(
        windowHandle,
        (videoMode!!.width() - width) / 2,
        (videoMode.height() - height) / 2
    )

    GLFW.glfwMakeContextCurrent(windowHandle)
    // Enable v-sync
    // Enable v-sync
    GLFW.glfwSwapInterval(1)

    // Make the window visible

    // Make the window visible
    GLFW.glfwShowWindow(windowHandle)

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the ContextCapabilities instance and makes the OpenGL
    // bindings available for use.

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the ContextCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities()

    // Set the clear color

    // Set the clear color
    GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
}

fun loop() {
    ShaderProgram(
        readFileAsText("$SHADERS_BASE_PATH/vertex.glsl"),
        readFileAsText("$SHADERS_BASE_PATH/fragment.glsl")
    ).bind()

    val vaoAndTexture: Pair<Int?, Int?> = loadTexture()

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!GLFW.glfwWindowShouldClose(windowHandle)) {
        GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
        vaoAndTexture.second?.let { GL11.glBindTexture(GL11.GL_TEXTURE_2D, it) }
        vaoAndTexture.first?.let { GL30.glBindVertexArray(it) }
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        GLFW.glfwSwapBuffers(windowHandle) // swap the color buffers
        GLFW.glfwPollEvents()
    }
}

fun run() {
    try {
        init()
        loop()
    } finally {
        // Release window and window callbacks
        // Release window and window callbacks
        Callbacks.glfwFreeCallbacks(windowHandle)
        GLFW.glfwDestroyWindow(windowHandle)
        // Terminate GLFW and release the GLFWerrorfun
        // Terminate GLFW and release the GLFWerrorfun
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)!!.free()
        GL.setCapabilities(null)
    }
}

fun main(args: Array<String>) {
    run()
}