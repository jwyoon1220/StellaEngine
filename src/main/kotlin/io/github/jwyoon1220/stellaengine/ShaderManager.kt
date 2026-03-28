package io.github.jwyoon1220.stellaengine

import io.github.jwyoon1220.stellaengine.entity.Material
import io.github.jwyoon1220.stellaengine.lighting.DirectionalLight
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
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
    @Throws(RuntimeException::class)
    fun createMaterialUniform(uniformName: String) {
        createUniform("$uniformName.ambient")
        createUniform("$uniformName.diffuse")
        createUniform("$uniformName.specular")
        createUniform("$uniformName.hasTexture")
        createUniform("$uniformName.reflectance")

    }
    // 예외 메시지를 상세하게 출력하는 공통 함수
    private fun getUniformLocation(uniformName: String): Int {
        return uniforms[uniformName]
            ?: throw RuntimeException("Could not find uniform location for: [$uniformName]. Check if it's used in shader or misspelled.")
    }

    @Throws(RuntimeException::class)
    fun createDirectionalLightUniform(uniformName: String) {
        createUniform("$uniformName.color")
        createUniform("$uniformName.direction")
        createUniform("$uniformName.intensity")
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        MemoryStack.stackPush().use { stack ->
            val location = getUniformLocation(uniformName)
            GL20.glUniformMatrix4fv(location, false, value.get(stack.mallocFloat(16)))
        }
    }

    fun setUniform(uniformName: String, value: Int) {
        GL20.glUniform1i(getUniformLocation(uniformName), value)
    }

    fun setUniform(uniformName: String, value: Float) { // Float 버전도 있으면 좋음
        GL20.glUniform1f(getUniformLocation(uniformName), value)
    }

    fun setUniform(uniformName: String, value: Vector3f) {
        GL20.glUniform3f(getUniformLocation(uniformName), value.x, value.y, value.z)
    }

    fun setUniform(uniformName: String, value: Vector4f) {
        GL20.glUniform4f(getUniformLocation(uniformName), value.x, value.y, value.z, value.w)
    }
    fun setUniform(uniformName: String, value: Boolean) {
        var res = 0f
        if (value) {
            res = 1f
        }
        GL20.glUniform1f(uniforms[uniformName] ?: throw RuntimeException("null"), res)
    }

    fun setUniform(uniformName: String, mat: Material) {
        setUniform("$uniformName.ambient", mat.ambientColor)
        setUniform("$uniformName.diffuse", mat.diffuseColor)
        setUniform("$uniformName.specular", mat.specularColor)
        setUniform("${uniformName}.hasTexture", if (mat.hasTexture()) 1 else 0)
        setUniform("$uniformName.reflectance", mat.reflectance)

    }
    fun setUniform(uniformName: String, dirLight: DirectionalLight) {
        setUniform("$uniformName.color", dirLight.color)
        setUniform("$uniformName.direction", dirLight.direction)
        setUniform("$uniformName.intensity", dirLight.intensity)
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