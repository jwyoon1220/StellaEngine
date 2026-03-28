package io.github.jwyoon1220.stellaengine.entity

import org.joml.Vector4f

class Model(
    val id: Int,
    val vertexCount: Int,
    // 기본값으로 빈 Material을 넣어 null을 원천 차단합니다.
    var material: Material = Material(Vector4f(1f, 1f, 1f, 1f), 0f)
) {
    init {
        require(vertexCount >= 0) { "Vertex count cannot be negative: $vertexCount" }
    }

    // 보조 생성자들
    constructor(model: Model, material: Material) : this(
        id = model.id,
        vertexCount = model.vertexCount,
        material = material
    )

    // 기존 자바 코드와의 호환성을 위한 생성자
    constructor(id: Int, length: Int) : this(id, length, Material(Vector4f(1f, 1f, 1f, 1f), 0f))

    /**
     * 이제 material이 null일 리 없으므로 체크 없이 바로 할당 가능합니다.
     */
    fun setTexture(texture: Texture) {
        this.material.texture = texture
    }

    fun setTexture(texture: Texture, reflectance: Float) {
        this.material.texture = texture
        this.material.reflectance = reflectance
    }

    fun getTexture(): Texture? {
        return this.material.texture
    }
}