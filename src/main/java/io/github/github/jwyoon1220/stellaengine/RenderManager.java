package io.github.github.jwyoon1220.stellaengine;

import io.github.github.jwyoon1220.stellaengine.entity.Model;
import io.github.github.jwyoon1220.stellaengine.utils.Utils;
import io.github.jwyoon1220.stellaengine.ShaderManager;
import org.lwjgl.opengl.*;

public class RenderManager {
    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager() {
        this.window = Launcher.getWindowManager();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.glsl"));
        shader.link();
        shader.createUniform("textureSampler");
    }

    public void render(Model model) {
        clear();
        shader.bind();
        shader.setUniform("textureSampler", 0);
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL30.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    public void dispose() {
        if (shader != null) {
            shader.dispose();
        }
    }

}
