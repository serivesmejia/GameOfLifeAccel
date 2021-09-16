package com.github.serivesmejia.accelgameoflife

import com.github.serivesmejia.accelgameoflife.gl.LifeTexture
import com.github.serivesmejia.accelgameoflife.gl.Square
import com.github.serivesmejia.accelgameoflife.gl.VAO
import com.github.serivesmejia.accelgameoflife.util.shaderFromResources
import org.joml.Vector2f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class Grid(val window: Window) {

    companion object {
        val lifeShader by lazy {
            val shader = shaderFromResources("vertex.glsl", "fragment.glsl")
            shader.texture = 0

            shader
        }
    }

    val square by lazy { Square(Vector2f(20f, 20f), Life.resolution.toFloat()) }

    val lifeTextureA by lazy { LifeTexture(Life.resolution) }
    val lifeTextureB by lazy { LifeTexture(Life.resolution) }

    fun draw() {
        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, lifeTextureA.textureId);

        lifeShader.bind()
        lifeShader.projection = window.projectionMatrix

        square.draw()

        lifeShader.unbind()

        glBindTexture(GL_TEXTURE_2D,0)
    }

}