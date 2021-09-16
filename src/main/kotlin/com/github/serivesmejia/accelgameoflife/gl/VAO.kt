package com.github.serivesmejia.accelgameoflife.gl

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

class VAO(vertices: FloatArray, textCoords: FloatArray) {

    var vaoID = 0
        private set
    var vboID = 0
        private set

    var verticesCount = 0
        private set

    init {
        val data = floatToFloatBuffer(vertices)

        // VAO creation
        vaoID = glGenVertexArrays()
        // Bind the VAO
        glBindVertexArray(vaoID)

        // VBO creation
        vboID = glGenBuffers()

        val textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.size)
        textCoordsBuffer.put(textCoords).flip()

        glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

        // Bind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        // Push the vertices array in the VBO
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)
        // Push the VBO in the VAO at index 0
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        // Deselect all
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        verticesCount = vertices.size / 3
    }

    fun updateVertices(vertices: FloatArray) {
        // Bind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboID)

        // update the data
        glBufferSubData(GL_ARRAY_BUFFER, 0L, floatToFloatBuffer(vertices))

        // unbind
        glBindVertexArray(0)
    }

    fun destroy() {
        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(vboID)
        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoID)
    }

}

fun floatToFloatBuffer(vertices: FloatArray): FloatBuffer {
    val verticesBuffer = BufferUtils.createFloatBuffer(vertices.size)
    verticesBuffer.put(vertices)
    verticesBuffer.flip()

    return verticesBuffer
}