package io.github.jwyoon1220.stellaengine;

import io.github.jwyoon1220.stellaengine.test.TestGame;
import io.github.jwyoon1220.stellaengine.utils.Const;
import org.lwjgl.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Launcher {
    private static WindowManager wm;
    private static TestGame game;

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Launcher.class);
        logger.info("Hello LWJGL {}!", Version.getVersion());
        wm = new WindowManager(Const.TITLE, Const.WIDTH, Const.HEIGHT, true);
        game = new TestGame();

        EngineManager engine = new EngineManager();
        try {
            engine.start();
        } catch (Exception e) {
            logger.error("An error occurred while starting the engine", e);
        }
    }

    public static TestGame getGame() {
        return game;
    }

    public static WindowManager getWindowManager() {
        return wm;
    }
}
