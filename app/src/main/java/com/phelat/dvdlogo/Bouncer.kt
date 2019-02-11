package com.phelat.dvdlogo

import android.os.Handler

class Bouncer(
    var speed: Int,
    private val dvdEntityX: DvdLogoState,
    private val dvdEntityY: DvdLogoState,
    var onChangeDirection: () -> Unit = {},
    var onMove: (x: DvdLogoState, y: DvdLogoState) -> Unit = { _, _ -> }
) {

    private val handler = Handler()

    private val runnable = Runnable {
        move()
    }

    var isRunning: Boolean = false
        set(value) {
            field = value
            if (value) {
                handler.postDelayed(runnable, speed.toLong())
            } else {
                handler.removeCallbacks(runnable)
            }
        }

    private fun move() {
        dvdEntityX.apply { dimensionInSafeArea += movementSpeed }
        dvdEntityY.apply { dimensionInSafeArea += movementSpeed }
        onMove(dvdEntityX, dvdEntityY)
        checkBorder(dvdEntityX)
        checkBorder(dvdEntityY)
    }

    private fun checkBorder(dvdEntity: DvdLogoState) {
        dvdEntity.apply {
            val isBitmapOnEndBorder = dimensionInSafeArea + bitmapDimension >= screenDimension
            val isBitmapOnStartBorder = dimensionInSafeArea <= 0
            if (isBitmapOnEndBorder) {
                reverseDirection(this)
                dimensionInSafeArea = screenDimension - bitmapDimension
            } else if (isBitmapOnStartBorder) {
                reverseDirection(this)
                dimensionInSafeArea = 0
            }
        }
    }

    private fun reverseDirection(dvdEntity: DvdLogoState) {
        dvdEntity.apply {
            movementSpeed = movementSpeed.unaryMinus()
            onChangeDirection()
        }
    }

    fun onMove(onMove: (x: DvdLogoState, y: DvdLogoState) -> Unit) {
        this.onMove = onMove
    }

    fun onChangeDirection(onChangeDirection: () -> Unit) {
        this.onChangeDirection = onChangeDirection
    }

}
