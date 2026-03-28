#version 400 core

in vec2 fragTextureCoords;
in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColor;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};
struct DirectionalLight {
    vec3 direction;
    vec3 color;
    float intensity;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setUpColors(Material material, vec2 textureCoords) {
    if (material.hasTexture == 1) {
        ambientC = texture(textureSampler, textureCoords);
        diffuseC = ambientC;
        specularC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}
vec4 calcLightColor(vec3 lightColor, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal) {
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);

    float diffuesFactor = max(dot(normal, toLightDir), 0.0);
    diffuseColor = diffuseC * vec4(lightColor, 1.0) * lightIntensity * diffuesFactor;

    vec3 camraDir = normalize(-position);
    vec3 fromLightDir = -toLightDir;
    vec3 reflectedLight = reflect(fromLightDir, normal);
    float specularFactor = max(dot(camraDir, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = specularC * lightIntensity * specularFactor * material.reflectance * vec4(lightColor, 1.0);
    return (diffuseColor + specularColor);
}
vec4 calcDirectinoalLight(DirectionalLight directionalLight, vec3 position, vec3 normal) {
    return calcLightColor(directionalLight.color.rgb, directionalLight.intensity, position, normalize(directionalLight.direction), normal);
}


void main() {
    setUpColors(material, fragTextureCoords);

    vec4 diffuseSpeclarComp = calcDirectinoalLight(directionalLight, fragPos, fragNormal);

    fragColor = ambientC * vec4(ambientLight, 1.0) + diffuseSpeclarComp;
}
