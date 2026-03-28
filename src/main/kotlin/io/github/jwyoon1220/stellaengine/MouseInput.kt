package io.github.jwyoon1220.stellaengine

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

class MouseInput {
    val previousPositon: Vector2d = Vector2d(-1.0, -1.0)
    val currentPositon: Vector2d = Vector2d(0.0, 0.0)

    val displayVec: Vector2f = Vector2f()

    var inWindow: Boolean = false
    private var leftButtonPressed: Boolean = false
    private var rightButtonPressed: Boolean = false

    fun init() {
        GLFW.glfwSetCursorPosCallback(Launcher.getWindowManager().hWnd) { _, xPos, yPos ->
            currentPositon.x = xPos
            currentPositon.y = yPos
        }
        GLFW.glfwSetCursorEnterCallback(Launcher.getWindowManager().hWnd) { _, entered ->
            inWindow = entered
        }
        GLFW.glfwSetMouseButtonCallback(Launcher.getWindowManager().hWnd) { _, button, action, _ ->
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS
        }
    }
    fun input() {
        displayVec.x = 0f
        displayVec.y = 0f

        // 마우스가 윈도우 안에 있고, 이전 위치가 기록되었을 때만 계산
        if (previousPositon.x >= 0 && previousPositon.y >= 0 && inWindow) {
            val dx = currentPositon.x - previousPositon.x
            val dy = currentPositon.y - previousPositon.y

            // 보통 마우스의 좌우 이동(dx)은 카메라의 도리도리(Yaw, rotation.y)를 바꿉니다.
            // 마우스의 상하 이동(dy)은 카메라의 끄덕끄덕(Pitch, rotation.x)을 바꿉니다.
            if (dx != 0.0) displayVec.y = dx.toFloat()
            if (dy != 0.0) displayVec.x = dy.toFloat()
        }

        // 매 프레임 현재 위치를 이전 위치로 업데이트
        previousPositon.x = currentPositon.x
        previousPositon.y = currentPositon.y
    }
    fun isLeftButtonPressed(): Boolean = leftButtonPressed
    fun isRightButtonPressed(): Boolean = rightButtonPressed

}