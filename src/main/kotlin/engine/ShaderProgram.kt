package engine

import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

class ShaderProgram(vertexShaderCode: String, fragmentShaderCode: String) {
    private val programId: Int = glCreateProgram()
    private val vertexShaderId: Int = createShader(vertexShaderCode, GL_VERTEX_SHADER, programId)
    private val fragmentShaderId: Int = createShader(fragmentShaderCode, GL_FRAGMENT_SHADER, programId)

    init {
        glLinkProgram(programId)
        logStatus(programId, GL_LINK_STATUS)
        if (vertexShaderId.toLong() != MemoryUtil.NULL) {
            glDetachShader(programId, vertexShaderId)
        }
        if (fragmentShaderId.toLong() != MemoryUtil.NULL) {
            glDetachShader(programId, fragmentShaderId)
        }
        glValidateProgram(programId)
        logStatus(programId, GL_VALIDATE_STATUS)
    }

    fun bind() {
        glUseProgram(programId);
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId.toLong() != MemoryUtil.NULL) {
            glDeleteProgram(programId)
        }
    }

    companion object {
        private fun createShader(shaderCode: String, shaderType: Int, programId: Int): Int {
            val shaderId = glCreateShader(shaderType)
            glShaderSource(shaderId, shaderCode)
            glCompileShader(shaderId)
            logStatus(shaderId, GL_COMPILE_STATUS)
            glAttachShader(programId, shaderId)
            return shaderId
        }

        private fun logStatus(id: Int, statusToCheck: Int) {
            MemoryStack.stackPush().use { stack ->
                val infoLog = stack.malloc(256 * Integer.BYTES)
                val infoLogLength = IntArray(256)
                when (statusToCheck) {
                    GL_COMPILE_STATUS -> {
                        if (glGetShaderi(id, statusToCheck).toLong() == MemoryUtil.NULL) {
                            glGetShaderInfoLog(id, infoLogLength, infoLog)
                        }
                    }
                    GL_LINK_STATUS -> {
                        if (glGetProgrami(id, statusToCheck).toLong() == MemoryUtil.NULL) {
                            glGetProgramInfoLog(id, infoLogLength, infoLog)
                        }
                    }
                    GL_VALIDATE_STATUS -> {
                        if (glGetProgrami(id, statusToCheck).toLong() == MemoryUtil.NULL) {
                            glGetProgramInfoLog(id, infoLogLength, infoLog)
                        }
                    }
                    else -> {
                        throw RuntimeException("No recognized log action has been sent: $statusToCheck")
                    }
                }
                println(infoLog.asReadOnlyBuffer().toString())
            }
        }
    }
}