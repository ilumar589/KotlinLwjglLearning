import org.joml.Math
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

fun triangleVertexArrayBuffer(): Int {
    val triangleData = floatArrayOf(
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.0f, 0.5f, 0.0f
    )
    val vao: Int = GL30.glGenVertexArrays() // consider a vertex array an object that holds the state of one vertex buffer object,
    // by state meaning all the vertex attribute pointers towards the vertex buffer object data
    val vbo: Int = GL15.glGenBuffers()
    GL30.glBindVertexArray(vao)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, triangleData, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * java.lang.Float.BYTES, 0)
    GL20.glEnableVertexAttribArray(0) // we have to enable each attribute pointer
    GL30.glBindVertexArray(0)
    return vao
}

fun squareWithElementBufferObject(): Int {
    // a square is composed of two triangles but seeing as their vertices intersect we
    // can use something called indexed drawing with an element buffer object
    val vertices = floatArrayOf(
        0.5f, 0.5f, 0.0f,  // top right
        0.5f, -0.5f, 0.0f,  // bottom right
        -0.5f, -0.5f, 0.0f,  // bottom left
        -0.5f, 0.5f, 0.0f // top left
    )
    val indices = intArrayOf( // note that we start from 0!
        0, 1, 3,  // first triangle
        1, 2, 3 // second triangle
    )
    val vao: Int = GL30.glGenVertexArrays()
    val vbo: Int = GL15.glGenBuffers()
    val ebo: Int = GL15.glGenBuffers()

    // ..:: Initialization code :: ..
    // 1. bind Vertex Array Object
    GL30.glBindVertexArray(vao)
    // 2. copy our vertices array in a vertex buffer for OpenGL to use
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
    // 3. copy our index array in a element buffer for OpenGL to use
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW)
    // 4. then set the vertex attributes pointers
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * java.lang.Float.BYTES, 0)
    GL20.glEnableVertexAttribArray(0) // we have to enable each attribute pointer
    GL30.glBindVertexArray(0)
    return vao
}

// Try to draw 2 triangles next to each other using glDrawArrays by adding more vertices to your data
fun helloTriangleExercise1(): Int {
    val vertices = floatArrayOf(
        -1.0f, 0.0f, 0.0f,  // first triangle bottom left
        -0.5f, 1.0f, 0.0f,  // first triangle top
        0.0f, 0.0f, 0.0f,  // first triangle bottom right
        0.04f, 0.0f, 0.0f,  // second triangle bottom left
        0.5f, 1.0f, 0.0f,  // second triangle top
        1.0f, 0.0f, 0.0f // second triangle bottom right
    )
    val vao: Int = GL30.glGenVertexArrays()
    val vbo: Int = GL15.glGenBuffers()
    GL30.glBindVertexArray(vao)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * java.lang.Float.BYTES, 0)
    GL20.glEnableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
    return vao

//        glDrawArrays(GL_TRIANGLES, 0, 6); used in loop
}

// Same two triangles using separate vaos and vbos
fun helloTriangleExercise2(): Pair<Int?, Int?> {
    val firstTriangleVertices = floatArrayOf(
        -1.0f, 0.0f, 0.0f,  // first triangle bottom left
        -0.5f, 1.0f, 0.0f,  // first triangle top
        0.0f, 0.0f, 0.0f
    )
    val firstTriangleVao: Int = GL30.glGenVertexArrays()
    val firstTriangleVbo: Int = GL15.glGenBuffers()
    GL30.glBindVertexArray(firstTriangleVao)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, firstTriangleVbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, firstTriangleVertices, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * java.lang.Float.BYTES, 0)
    GL20.glEnableVertexAttribArray(0)
    val secondTriangleVertices = floatArrayOf(
        0.04f, 0.0f, 0.0f,  // second triangle bottom left
        0.5f, 1.0f, 0.0f,  // second triangle top
        1.0f, 0.0f, 0.0f // second triangle bottom right
    )
    val secondTriangleVao: Int = GL30.glGenVertexArrays()
    val secondTriangleVbo: Int = GL15.glGenBuffers()
    GL30.glBindVertexArray(secondTriangleVao)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, secondTriangleVbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, secondTriangleVertices, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * java.lang.Float.BYTES, 0)
    GL20.glEnableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
    return Pair(firstTriangleVao, secondTriangleVao)
}

fun colorOnAttributes(): Int {
    val vertices = floatArrayOf( //positions          //colors
        0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,  // bottom right
        -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,  // bottom left
        0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f // top
    )
    val vao = GL30.glGenVertexArrays()
    val vbo = GL15.glGenBuffers()
    GL30.glBindVertexArray(vao)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * java.lang.Float.BYTES, 0)
    GL20.glEnableVertexAttribArray(0)
    GL20.glVertexAttribPointer(
        1,
        3,
        GL11.GL_FLOAT,
        false,
        6 * java.lang.Float.BYTES,
        (3 * java.lang.Float.BYTES).toLong()
    )
    GL20.glEnableVertexAttribArray(1)
    GL30.glBindVertexArray(0)
    return vao
}

fun loadTexture(): Pair<Int?, Int?> {
    MemoryStack.stackPush().use { stack ->
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val nrOfChannels = stack.mallocInt(1)
        val texture = GL11.glGenTextures()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
        // set the texture wrapping/filtering options (on the currently bound texture object)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
        val textureData: ByteBuffer? =
            STBImage.stbi_load("$TEXTURES_BASE_PATH/container.jpg", width, height, nrOfChannels, 0)
        textureData?.let {
            if (it.hasRemaining()) {
                GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RGB,
                    width.get(),
                    height.get(),
                    0,
                    GL11.GL_RGB,
                    GL11.GL_UNSIGNED_BYTE,
                    it
                )
                GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
                STBImage.stbi_image_free(textureData)
            } else {
                println("Failed to load texture")
            }
        }
        val vertices = floatArrayOf( // positions          // colors           // texture coords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,  // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,  // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f // top left
        )
        val vao = GL30.glGenVertexArrays()
        val vbo = GL15.glGenBuffers()
        GL30.glBindVertexArray(vao)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * java.lang.Float.BYTES, 0)
        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(
            1,
            3,
            GL11.GL_FLOAT,
            false,
            8 * java.lang.Float.BYTES,
            (3 * java.lang.Float.BYTES).toLong()
        )
        GL20.glEnableVertexAttribArray(1)
        GL20.glVertexAttribPointer(
            2,
            2,
            GL11.GL_FLOAT,
            false,
            8 * java.lang.Float.BYTES,
            (6 * java.lang.Float.BYTES).toLong()
        )
        GL20.glEnableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
        return Pair(vao, texture)
    }
}

fun setUniformExample() {
    // update the uniform color
    val timeValue = GLFW.glfwGetTime().toFloat()
    val greenValue = Math.sin(timeValue) / 2.0f + 0.5f
    val vertexColorLocation = GL20.glGetUniformLocation(1, "ourColor")
    GL20.glUniform4f(vertexColorLocation, 0.0f, greenValue, 0.0f, 1.0f)
}