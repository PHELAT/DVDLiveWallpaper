package com.phelat.dvdlogo

import android.graphics.Color
import kotlin.random.Random

object ColorProvider {

    fun generateColor(): Int {
        return Color.argb(
            Random.nextInt(128, 256),
            Random.nextInt(50, 256),
            Random.nextInt(50, 256),
            Random.nextInt(50, 256)
        )
    }

}
