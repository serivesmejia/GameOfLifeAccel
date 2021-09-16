package com.github.serivesmejia.accelgameoflife.gl

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*

class Shader {

    val programId = glCreateProgram()

    var vertexId = 0
        private set
    var fragmentId = 0
        private set

    val projectionLocation by lazy { getUniformLocation("projection") }
    val texureSamplerLocation by lazy { getUniformLocation("texture_sampler") }

    init {
        if(programId == 0) {
            throw IllegalStateException("Could not create shader program")
        }
    }

    fun simpleCreate(vertex: String, fragment: String) {
        createVertexShader(vertex)
        createFragmentShader(fragment)
        link()
    }

    fun createVertexShader(code: String) {
        vertexId = createShader(code, GL_VERTEX_SHADER)
    }

    fun createFragmentShader(code: String) {
        fragmentId = createShader(code, GL_FRAGMENT_SHADER)
    }

    fun link() {
        glLinkProgram(programId)

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw IllegalStateException("Error while linking shader code:\n" + glGetProgramInfoLog(programId, 1024))
        }

        if (vertexId != 0) {
            glDetachShader(programId, vertexId)
        }
        if (fragmentId != 0) {
            glDetachShader(programId, fragmentId)
        }

        glValidateProgram(programId)

        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            println("Warn about validating shader code:\n" + glGetProgramInfoLog(programId, 1024))
        }
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun delete() {
        unbind()

        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

    fun loadMatrix(location: Int, value: Matrix4f) {
        val matrix = BufferUtils.createFloatBuffer(16)
        value.get(matrix)

        glUniformMatrix4fv(location, false, matrix)
    }

    fun getUniformLocation(name: String) = glGetUniformLocation(programId, name)

    fun bindAttribute(attribute: Int, name: String) {
        glBindAttribLocation(programId, attribute, name)
    }

    fun setUniform(location: Int, value: Int) {
        glUniform1i(location, value)
    }


    var texture: Int = 0
        set(value) {
            setUniform(texureSamplerLocation, value)
            field = value
        }

    var projection: Matrix4f = Matrix4f()
        set(value) {
            loadMatrix(projectionLocation, value)
            field = value
        }

    private fun createShader(code: String, type: Int): Int {
        val id = glCreateShader(type)

        if(id == 0) {
            throw IllegalStateException("Could not create shader")
        }

        glShaderSource(id, code)
        glCompileShader(id)

        if(glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            throw IllegalStateException("Error while compiling shader:\n${glGetShaderInfoLog(id, 1024)}")
        }

        glAttachShader(programId, id)

        return id
    }

}