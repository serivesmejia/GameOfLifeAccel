package com.github.serivesmejia.accelgameoflife.util

import com.github.serivesmejia.accelgameoflife.gl.Shader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

fun resourceAsString(resourcePath: String): String? {
    val classLoader = ClassLoader.getSystemClassLoader()

    classLoader.getResourceAsStream(resourcePath).use { ins ->
        if (ins == null) return null
        InputStreamReader(ins).use { isr ->
            BufferedReader(isr).use { reader ->
                return reader.lines().collect(Collectors.joining(System.lineSeparator()))
            }
        }
    }
}

fun shaderFromResources(pathVert: String, pathFrag: String) = Shader().apply {
    simpleCreate(
        resourceAsString(pathVert)!!,
        resourceAsString(pathFrag)!!
    )
}