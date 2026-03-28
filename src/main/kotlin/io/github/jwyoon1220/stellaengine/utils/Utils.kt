package io.github.jwyoon1220.stellaengine.utils

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

object Utils {

    /**
     * FloatArray를 FloatBuffer로 변환하고 flip합니다.
     */
    @JvmStatic
    fun storeDataInFloatBuffer(data: FloatArray): FloatBuffer = data.toFlippedBuffer()

    /**
     * IntArray를 IntBuffer로 변환하고 flip합니다.
     */
    @JvmStatic
    fun storeDataInIntBuffer(data: IntArray): IntBuffer = data.toFlippedBuffer()

    /**
     * 클래스패스에서 리소스를 읽어 문자열로 반환합니다.
     */
    @JvmStatic
    fun loadResource(fileName: String): String? =
        Utils::class.java.getResourceAsStream(fileName)?.bufferedReader()?.use { it.readText() }

    // --- 내부적으로 사용하는 확장 함수 (Private 또는 Public 선택 가능) ---

    fun FloatArray.toFlippedBuffer(): FloatBuffer =
        MemoryUtil.memAllocFloat(size).apply {
            put(this@toFlippedBuffer).flip()
        }

    fun IntArray.toFlippedBuffer(): IntBuffer =
        MemoryUtil.memAllocInt(size).apply {
            put(this@toFlippedBuffer).flip()
        }

    @JvmStatic
    fun readAllLines(fileName: String): List<String> {
        // Class.forName 대신 현재 클래스의 context를 바로 사용합니다.
        // useLines는 시퀀스를 제공하며, 바로 toList()로 변환이 가능합니다.
        return Utils::class.java.getResourceAsStream(fileName)?.bufferedReader()?.useLines { lines ->
            lines.toList()
        } ?: emptyList() // 파일이 없을 경우 null 대신 빈 리스트 반환
    }
}