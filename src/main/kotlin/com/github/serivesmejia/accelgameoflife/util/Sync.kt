package com.github.serivesmejia.accelgameoflife.util

@Volatile private var start = 0.0
@Volatile private var diff = 0.0
@Volatile private var wait = 0.0

@Throws(InterruptedException::class)
fun sync(fps: Double) {
    wait = 1.0 / (fps / 1000.0)
    diff = System.currentTimeMillis() - start

    if (diff < wait) {
        Thread.sleep((wait - diff).toLong())
    }

    start = System.currentTimeMillis().toDouble()
}
