package com.github.serivesmejia.accelgameoflife.gl

import com.github.serivesmejia.accelgameoflife.Life
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.system.MemoryUtil
import java.util.*

class LifeTexture(size: Int) {

    val buffer = ByteArray(size * size * 4)

    val random = Random(System.currentTimeMillis())

    init {
        var i = 0

        while(i < buffer.size) {
            val v = if(random.nextBoolean()) 255.toByte() else 0.toByte()

            buffer[i] = v

            i += 1
        }
    }

    var textureId = glGenTextures()

    init {
        glBindTexture(GL_TEXTURE_2D, textureId)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        val b = BufferUtils.createByteBuffer(buffer.size)
        b.put(buffer)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_RGBA, GL_UNSIGNED_BYTE, b)

        glGenerateMipmap(GL_TEXTURE_2D)
    }

}