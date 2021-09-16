package com.github.serivesmejia.accelgameoflife.gl

import org.joml.Vector2f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class Square(
    val position: Vector2f,
    val size: Float
) {

    val vao = VAO(makeVertices())

    fun draw() {
        GL30.glBindVertexArray(vao.vaoID)
        GL20.glEnableVertexAttribArray(0)
        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vao.verticesCount)
        // Put everything back to default (deselect)
        GL20.glDisableVertexAttribArray(0)
        GL30.glBindVertexArray(0)
    }

    // I KNOW I SHOULD BE USING INDICES
    // but... we are only going to have one textured square to render so...?
    // what's the point of putting the effort? saving a very few bytes? :ha:
    fun makeVertices() = floatArrayOf(
        // left triangle
        position.x,        position.y - size, 0f,
        position.x + size, position.y,        0f,
        position.x,        position.y,        0f,
        // right triangle
        position.x,        position.y - size, 0f,
        position.x + size, position.y - size, 0f,
        position.x + size, position.y,        0f
    )

}