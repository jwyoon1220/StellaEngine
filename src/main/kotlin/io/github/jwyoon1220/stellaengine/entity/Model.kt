package io.github.jwyoon1220.stellaengine.entity

class Model {
    val id: Int
    val vertexCount: Int
    var texture: Texture? = null

    constructor(id: Int, vertexCount: Int) {
        this.id = id
        this.vertexCount = vertexCount
    }

    constructor(id: Int, vertexCount: Int, texture: Texture?) {
        this.id = id
        this.vertexCount = vertexCount
        this.texture = texture
    }

    constructor(model: Model, texture: Texture?) {
        this.id = model.id
        this.vertexCount = model.vertexCount
        this.texture = texture
    }
}