package io.github.jwyoon1220.stellaengine.entity

import org.joml.Vector3f

data class Entity(
    val model: Model?,
    val pos: Vector3f,
    val rotation: Vector3f,
    val scale: Float
) {
    fun incPos(x: Float, y: Float, z: Float) {
        this.pos.x += x
        this.pos.y += y
        this.pos.z += z
    }

    fun setPos(x: Float, y: Float, z: Float) {
        this.pos.x = x
        this.pos.y = y
        this.pos.z = z
    }

    fun incRotation(x: Float, y: Float, z: Float) {
        this.rotation.x += x
        this.rotation.y += y
        this.rotation.z += z
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        this.rotation.x = x
        this.rotation.y = y
        this.rotation.z = z
    }
}