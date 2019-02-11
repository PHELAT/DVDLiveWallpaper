package com.phelat.dvdlogo.provider

import android.graphics.Bitmap
import android.util.DisplayMetrics
import com.phelat.dvdlogo.Bouncer
import com.phelat.dvdlogo.state.DvdLogoState
import com.phelat.dvdlogo.OptionsConstant
import kotlin.random.Random

class BouncerProvider {

    fun provide(displayMetrics: DisplayMetrics, bitmap: Bitmap): Bouncer {
        return displayMetrics.run {
            val canvasSafeAreaForX = widthPixels - bitmap.width
            val canvasSafeAreaForY = heightPixels - bitmap.height

            val bitmapInitialX = Random.nextInt(bitmap.width, canvasSafeAreaForX)
            val bitmapInitialY = Random.nextInt(bitmap.height, canvasSafeAreaForY)

            val dvdEntityX = DvdLogoState(
                bitmapInitialX,
                bitmap.width,
                widthPixels,
                10
            )
            val dvdEntityY = DvdLogoState(
                bitmapInitialY,
                bitmap.height,
                heightPixels,
                10
            )

            Bouncer(
                OptionsConstant.MOVEMENT_SPEED_DEFAULT,
                dvdEntityX,
                dvdEntityY
            )
        }
    }

}
