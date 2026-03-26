package io.github.jwyoon1220.stellaengine

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import kotlin.jvm.Throws

class ShaderManager {
    private val programId: Int = GL20.glCreateProgram()
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    private val uniforms: HashMap<String, Int> = HashMap()

    init {
        if (programId == 0) {
            throw RuntimeException("Could not create Shader")
        }
    }

    @Throws(RuntimeException::class)
    fun createUniform(name: String) {
        val unifromLocation = GL20.glGetUniformLocation(programId, name)
        if (unifromLocation < 0) {
            throw RuntimeException("Could not find uniform: $name")
        }
        uniforms[name] = unifromLocation
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        MemoryStack.stackPush().use { stack ->
            GL20.glUniformMatrix4fv(uniforms[uniformName] ?: throw RuntimeException("null"), false, value.get(stack.mallocFloat(16)))
        }
    }
    fun setUniform(uniformName: String, value: Int) {
        MemoryStack.stackPush().use { stack ->
            GL20.glUniform1i(uniforms[uniformName] ?: throw RuntimeException("null"), value)
        }
    }

    @Throws(RuntimeException::class)
    fun createVertexShader(vertexSource: String) {
        vertexShaderId = createShader(vertexSource, GL20.GL_VERTEX_SHADER)
    }
    @Throws(RuntimeException::class)
    fun createFragmentShader(fragmentSource: String) {
        fragmentShaderId = createShader(fragmentSource, GL20.GL_FRAGMENT_SHADER)
    }
    @Throws(RuntimeException::class)
    fun createShader(shaderCode: String, shaderType: Int): Int {
        val sid = GL20.glCreateShader(shaderType)
        if (sid == 0) {
            throw RuntimeException("Could not create shader: type:${shaderType}")
        }
        GL20.glShaderSource(sid, shaderCode)
        GL20.glCompileShader(sid)

        if (GL20.glGetShaderi(sid, GL20.GL_COMPILE_STATUS) == 0) {
            throw RuntimeException("Error compiling Shader code: ${GL20.glGetShaderInfoLog(sid, 1024)}")
        }
        GL20.glAttachShader(programId, sid)
        return sid
    }

    @Throws(RuntimeException::class)
    fun link() {
        GL20.glLinkProgram(programId)
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            throw RuntimeException("Error linking Shader code: ${GL20.glGetProgramInfoLog(programId, 1024)}")
        }
        if (vertexShaderId != 0) {
            GL20.glDetachShader(programId, vertexShaderId)
        }
        if (fragmentShaderId != 0) {
            GL20.glDetachShader(programId, fragmentShaderId)
        }
        GL20.glValidateProgram(programId)
        if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == 0) {
            throw RuntimeException("Warning validating Shader code: ${GL20.glGetProgramInfoLog(programId, 1024)}")
        }
    }

    fun bind() {
        GL20.glUseProgram(programId)
    }
    fun unbind() {
        GL20.glUseProgram(0)
    }
    fun dispose() {
        unbind()
        if (programId != 0) {
            GL20.glDeleteProgram(programId)
        }
    }

}