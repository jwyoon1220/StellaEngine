package io.github.jwyoon1220.stellaengine.lighting

import org.joml.Vector3f

data class DirectionalLight(
    var color: Vector3f,
    var direction: Vector3f,
    var intensity: Float
)