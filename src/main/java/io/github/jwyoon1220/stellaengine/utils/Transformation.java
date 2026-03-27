package io.github.jwyoon1220.stellaengine.utils;

import io.github.jwyoon1220.stellaengine.entity.Entity;
import org.joml.Matrix4f;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        var matrix = new Matrix4f();
        matrix.identity().translate(entity.getPos())
                .rotateX((float) Math.toRadians(entity.getRotation().x))
                .rotateY((float) Math.toRadians(entity.getRotation().y))
                .rotateZ((float) Math.toRadians(entity.getRotation().z))
                .scale(entity.getScale());
        return matrix;
    }

}
