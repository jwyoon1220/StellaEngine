package io.github.jwyoon1220.stellaengine.entity;

import io.github.jwyoon1220.stellaengine.utils.Const;
import org.joml.Vector4f;

public class Material {
    private Vector4f ambientColor, diffuseColor, specularColor;
    private float reflectance;
    private Texture texture;

    public Material() {
        this.ambientColor = Const.DEFAULT_COLOR;
        this.diffuseColor = Const.DEFAULT_COLOR;
        this.specularColor = Const.DEFAULT_COLOR;
        this.reflectance = 0;
        this.texture = null;

    }
    public Material(Vector4f color, float reflectance) {
        this(color, color, color, reflectance, null);
    }
    public Material(Texture texture) {
        this(Const.DEFAULT_COLOR, Const.DEFAULT_COLOR, Const.DEFAULT_COLOR, 0, texture);
    }
    public Material(Vector4f color, float reflectance, Texture texture) {
        this(color, color, color, reflectance, texture);
    }
    public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, float reflectance, Texture texture) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.reflectance = reflectance;
        this.texture = texture;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
