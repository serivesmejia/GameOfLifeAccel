package com.github.serivesmejia.accelgameoflife.gl

import com.github.serivesmejia.accelgameoflife.Life
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.system.MemoryUtil

class LifeTexture(size: Int) {

    companion object {
        val buffer = BufferUtils.createByteBuffer(Life.resolution * Life.resolution)

        init {
            MemoryUtil.memSet(buffer, 120)
        }
    }

    var textureId = glGenTextures()

    init {
        glBindTexture(GL_TEXTURE_2D, textureId)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, size, size, 0, GL_RED, GL_UNSIGNED_BYTE, buffer)

        glGenerateMipmap(GL_TEXTURE_2D)
    }

}