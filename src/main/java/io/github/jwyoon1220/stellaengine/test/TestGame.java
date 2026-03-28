package io.github.jwyoon1220.stellaengine.test;

import io.github.jwyoon1220.stellaengine.*;
import io.github.jwyoon1220.stellaengine.entity.Entity;
import io.github.jwyoon1220.stellaengine.entity.Model;
import io.github.jwyoon1220.stellaengine.entity.Texture;
import io.github.jwyoon1220.stellaengine.lighting.DirectionalLight;
import io.github.jwyoon1220.stellaengine.utils.Const;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements Logic {

    private static final float CAMERA_SPEED = 0.02f;
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle;
    private DirectionalLight directionalLight;

    public TestGame() {
        renderer = new RenderManager();
        window = Launcher.getWindowManager();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;

    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/bunny.obj");
        model.setTexture(new Texture(loader.loadTexture("texture/grass.jpg")), 1f);
        entity = new Entity(model, new Vector3f(0f, 0f, -5f), new Vector3f(0f, 0f, 0f), 10f);
        float lightIntensity = 0f;
        Vector3f lightPosition = new Vector3f(-1, -10, 0);
        Vector3f lightColor = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColor, lightPosition, lightIntensity);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);

        // W가 앞으로(Z 감소), S가 뒤로(Z 증가) 가도록 수정
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraInc.z = 1;
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraInc.x = 1;
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_SPEED, cameraInc.y * CAMERA_SPEED, cameraInc.z * CAMERA_SPEED);

        // 2. 카메라 회전 (오른쪽 버튼 클릭 시)
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplayVec();
            // rotVec.x는 마우스 상하 이동(Pitch), rotVec.y는 마우스 좌우 이동(Yaw)
            camera.moveRotation(rotVec.x * Const.MOUSE_SENSITIVITY, rotVec.y * Const.MOUSE_SENSITIVITY, 0);
        }

        //entity.incRotation(0f, 0.25f, 0f);
        lightAngle += 0.5f;
        if (lightAngle > 90f) {
            directionalLight.setIntensity(0f);
            if (lightAngle >= 360f) {
                lightAngle = -90f;
            }
        } else if (lightAngle < -80f || lightAngle >= 80f) {
            float factor = 1 - (Math.abs(lightAngle) - 80f) / 10f; // Linear Decrease from 80.
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1f);
            directionalLight.getColor().x = 1f;
            directionalLight.getColor().y = 1f;
            directionalLight.getColor().z = 1f;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render() {
        renderer.render(entity, camera, directionalLight);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        loader.dispose();
    }
}
