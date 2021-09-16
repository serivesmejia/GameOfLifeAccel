package com.github.serivesmejia.accelgameoflife.gl

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer


class VAO(vertices: FloatArray) {

    var vaoID = 0
        private set
    var vboID = 0
        private set

    var verticesCount = 0
        private set

    init {
        // VAO creation
        vaoID = GL30.glGenVertexArrays()
        // Bind the VAO
        GL30.glBindVertexArray(vaoID)
        // VBO creation
        vboID = GL15.glGenBuffers()
        // Bind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
        // Push the vertices array in the VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatToFloatBuffer(vertices), GL15.GL_STATIC_DRAW)
        // Push the VBO in the VAO at index 0
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)
        // Deselect all
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL30.glBindVertexArray(0)
        verticesCount = vertices.size / 3
    }

    fun destroy() {
        // Delete the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL15.glDeleteBuffers(vboID)
        // Delete the VAO
        GL30.glBindVertexArray(0)
        GL30.glDeleteVertexArrays(vaoID)
    }

}

fun floatToFloatBuffer(vertices: FloatArray): FloatBuffer {
    val verticesBuffer = BufferUtils.createFloatBuffer(vertices.size)
    verticesBuffer.put(vertices)
    verticesBuffer.flip()

    return verticesBuffer
}