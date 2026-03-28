package io.github.jwyoon1220.stellaengine;

public interface Logic {
    void init() throws Exception;

    void input();
    void update(float interval, MouseInput mouseInput);
    void render();

    void dispose();
}
