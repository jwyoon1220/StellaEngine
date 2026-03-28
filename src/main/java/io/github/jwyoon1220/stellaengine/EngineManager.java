package io.github.jwyoon1220.stellaengine;

import io.github.jwyoon1220.stellaengine.utils.Const;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECONDS = 1000000000L;
    public static final float FRAMERATE = 1000f;

    private static int fps;
    private static final float frameTime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager wm;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;

    private Logic gameLogic;


    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        wm = Launcher.getWindowManager();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();

        wm.init();
        gameLogic.init();
        mouseInput.init();
    }
    public void start() throws Exception {
        init();
        if (isRunning) {
            return;
        }
        run();
    }

    private void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECONDS;
            frameCounter += passedTime;

            input();

            while (unprocessedTime > frameTime) {
                render = true;
                unprocessedTime -= frameTime;

                if (wm.windowShouldClose()) {
                    stop();
                }
                if (frameCounter >= NANOSECONDS) {
                    setFps(frames);
                    wm.setTitle(Const.TITLE + "fps: " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if (render) {
                update(frameTime);
                render();
                frames++;
            }
        }
        dispose();
    }
    private void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }
    private void input() {
        mouseInput.input();
        gameLogic.input();
    }
    private void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }
    private void render() {
        gameLogic.render();
        wm.update();
    }
    private void dispose() {
        wm.dispose();
        gameLogic.dispose();

        errorCallback.free();

        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
