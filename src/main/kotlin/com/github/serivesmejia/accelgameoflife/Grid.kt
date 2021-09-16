package com.github.serivesmejia.accelgameoflife

import com.github.serivesmejia.accelgameoflife.gl.Square
import com.github.serivesmejia.accelgameoflife.gl.VAO
import com.github.serivesmejia.accelgameoflife.util.shaderFromResources
import org.joml.Vector2f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class Grid(val window: Window) {

    companion object {
        val lifeShader by lazy { shaderFromResources("vertex.glsl", "fragment.glsl") }
    }

    var vertices = floatArrayOf(
        // Left bottom triangle
        -50f, 50f, 0f,
        -50f, -50f, 0f,
        50f, -50f, 0f,
        // Right top triangle
        50f, -50f, 0f,
        50f, 50f, 0f,
        -50f, 50f, 0f
    )

    val square by lazy { Square(Vector2f(20f, 20f), 80f) }

    fun draw() {
        lifeShader.bind()
        lifeShader.projection = window.projectionMatrix

        square.draw()

        lifeShader.unbind()
    }

}