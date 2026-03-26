package io.github.github.jwyoon1220.stellaengine;

public interface Logic {
    void init() throws Exception;

    void input();
    void update();
    void render();

    void dispose();
}
