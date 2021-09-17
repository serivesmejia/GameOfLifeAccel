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
import org.lwjgl.opengl.GL30.*

class Grid(val window: Window) {

    companion object {
        val lifeShader by lazy {
            val shader = shaderFromResources("vertex.glsl", "fragment.glsl")
            shader.texture = 0

            shader
        }

        val copyShader by lazy {
            val shader = shaderFromResources("vertex.glsl", "copy_fragment.glsl")
            shader.texture = 0

            shader
        }
    }

    val square by lazy { Square(Vector2f(0f, 0f), Life.resolution.toFloat()) }
    val lifeSquare by lazy { Square(Vector2f(0f, 0f), Life.resolution.toFloat()) }

    val lifeTextureA by lazy { LifeTexture(Life.resolution) }
    val lifeTextureB by lazy { LifeTexture(Life.resolution) }

    val frameBufferName by lazy {
        val buff = glGenFramebuffers()

        glBindFramebuffer(GL_FRAMEBUFFER, buff)
        GL20.glDrawBuffers(GL_COLOR_ATTACHMENT0)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)

        buff
    }

    var invert = false

    fun draw() {
        val aTexture = if(invert) lifeTextureB else lifeTextureA
        val bTexture = if(invert) lifeTextureA else lifeTextureB

        invert = !invert

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, bTexture.textureId, 0)
        glViewport(0, 0, Life.resolution, Life.resolution)

        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0)
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, aTexture.textureId)

        lifeShader.bind()
        lifeShader.projection = window.projectionMatrix

        lifeSquare.draw()

        lifeShader.unbind()

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        val size = window.size
        glViewport(0, 0, size.x.toInt(), size.y.toInt())

        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, bTexture.textureId)

        copyShader.bind()
        copyShader.projection = window.projectionMatrix

        square.draw()

        copyShader.unbind()

        glBindTexture(GL_TEXTURE_2D,0)
    }

}