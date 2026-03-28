package io.github.jwyoon1220.stellaengine;

import io.github.jwyoon1220.stellaengine.entity.Entity;
import io.github.jwyoon1220.stellaengine.lighting.DirectionalLight;
import io.github.jwyoon1220.stellaengine.utils.Const;
import io.github.jwyoon1220.stellaengine.utils.Transformation;
import io.github.jwyoon1220.stellaengine.utils.Utils;
import org.lwjgl.opengl.*;

import java.util.Objects;

public class RenderManager {
    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager() {
        this.window = Launcher.getWindowManager();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Objects.requireNonNull(Utils.INSTANCE.loadResource("/shaders/vertex.glsl")));
        shader.createFragmentShader(Objects.requireNonNull(Utils.INSTANCE.loadResource("/shaders/fragment.glsl")));
        shader.link();

        // 추가해야 할 코드들
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix"); // 추가
        shader.createUniform("viewMatrix");       // 추가
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material"); // 추가
        shader.createUniform("specularPower"); // 추가
        shader.createDirectionalLightUniform("directionalLight"); // 추가
    }

    public void render(Entity entity, Camera camera, DirectionalLight directionalLight) {
        if (entity == null) {
            return;
        } else {
            entity.getModel();
        }

        clear();
        shader.bind();

        // 1. 행렬들을 미리 계산하여 null 체크
        var worldMatrix = Transformation.createTransformationMatrix(entity);
        var projectionMatrix = window.updateProjectionMatrix();
        var viewMatrix = Transformation.getViewMatrix(camera);
        var material = entity.getModel().getMaterial();

        // 2. Uniform 설정 (에러 방지를 위한 검증)
        shader.setUniform("textureSampler", 0);

        shader.setUniform("transformationMatrix", worldMatrix);
        if (projectionMatrix != null) shader.setUniform("projectionMatrix", projectionMatrix);
        shader.setUniform("viewMatrix", viewMatrix);

        // Material이 null이면 NPE가 발생하므로 체크 필수
        shader.setUniform("material", material);

        shader.setUniform("ambientLight", Const.AMBIENT_LIGHT);

        shader.setUniform("specularPower", Const.SPECULAR_POWER);

        shader.setUniform("directionalLight", directionalLight);

        // --- 렌더링 루틴 ---
        GL30.glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL30.glActiveTexture(GL13.GL_TEXTURE0);
        if (entity.getModel().getTexture() != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
        }

        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
        shader.unbind();
    }
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0f, 0f, 0f, 0f);
    }
    public void dispose() {
        if (shader != null) {
            shader.dispose();
        }
    }

}
