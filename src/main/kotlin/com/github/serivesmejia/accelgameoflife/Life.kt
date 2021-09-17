package com.github.serivesmejia.accelgameoflife

import com.github.serivesmejia.accelgameoflife.util.FpsCount
import com.github.serivesmejia.accelgameoflife.util.sync
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

class Life {

    companion object {
        val resolution = 100
    }

    val window = Window()
    val fpsCount = FpsCount()

    val grid = Grid(window)

    fun start() {
        window.create()
        window.center()

        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA)

        while(!GLFW.glfwWindowShouldClose(window.ptr)) {
            loop()

            fpsCount.update()
            sync(60.0)
        }
    }

    private fun loop() {
        GL11.glClearColor(0f, 0f, 0f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        grid.draw()

        GLFW.glfwSwapBuffers(window.ptr)
        GLFW.glfwPollEvents()
    }

}

fun main() {
    Life().start()
}