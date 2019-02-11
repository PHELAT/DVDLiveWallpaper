package com.phelat.dvdlogo.provider

import android.graphics.Color
import kotlin.random.Random

object ColorProvider {

    fun provide(): Int {
        return Color.argb(
            Random.nextInt(128, 256),
            Random.nextInt(50, 256),
            Random.nextInt(50, 256),
            Random.nextInt(50, 256)
        )
    }

}
