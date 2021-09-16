package com.github.serivesmejia.accelgameoflife.util

class FpsCount {

    private val timer = ElapsedTime()

    var fps = 0
        private set

    private var count = 0

    fun update() {
        count++

        if(timer.seconds >= 1.0) {
            fps = count
            count = 0
            timer.reset()
        }
    }

}