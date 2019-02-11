package com.phelat.dvdlogo

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class DvdWallpaperService : WallpaperService() {

    private lateinit var context: Context

    private val sharedPreference by lazy(LazyThreadSafetyMode.NONE) {
        getSharedPreferences(OptionsConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private var isVisible = false

    private val bitmap by lazy(LazyThreadSafetyMode.NONE) {
        BitmapFactory.decodeResource(context.resources, R.drawable.dvd)
    }

    private lateinit var bouncer: Bouncer

    override fun onCreateEngine(): Engine {
        context = this
        bouncer = BouncerProvider().provide(context.resources.displayMetrics, bitmap)
        return DvdLogoEngine()
    }

    inner class DvdLogoEngine : Engine() {

        private var canvas: Canvas? = null

        private val paint by lazy(LazyThreadSafetyMode.NONE) {
            Paint().apply {
                isAntiAlias = true
                colorFilter = PorterDuffColorFilter(
                    Color.parseColor("#FFFFFF"),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }

        private fun setPaintColor() {
            paint.colorFilter = PorterDuffColorFilter(
                ColorProvider.generateColor(),
                PorterDuff.Mode.SRC_IN
            )
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            bouncer.speed = sharedPreference.getInt(
                OptionsConstant.MOVEMENT_SPEED_OPTION,
                OptionsConstant.MOVEMENT_SPEED_DEFAULT
            )
            setCallbacks()
            bouncer.isRunning = true
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            this@DvdWallpaperService.isVisible = visible
            if (visible && !bouncer.isRunning) {
                setCallbacks()
                bouncer.isRunning = true
            } else {
                bouncer.isRunning = false
                removeCallbacks()
            }
        }

        private fun drawBitmap(x: DvdLogoState, y: DvdLogoState) {
            canvas = null
            try {
                canvas = surfaceHolder.lockCanvas()
                if (canvas != null) {
                    canvas?.apply {
                        drawColor(Color.parseColor("#000000"))
                        drawBitmap(
                            bitmap,
                            x.dimensionInSafeArea.toFloat(),
                            y.dimensionInSafeArea.toFloat(),
                            paint
                        )
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
            bouncer.isRunning = false
            if (isVisible) {
                bouncer.isRunning = true
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            this@DvdWallpaperService.isVisible = false
            bouncer.isRunning = false
            removeCallbacks()
        }

        override fun onDestroy() {
            super.onDestroy()
            bouncer.isRunning = false
            removeCallbacks()
        }

        private fun setCallbacks() {
            bouncer.onChangeDirection {
                setPaintColor()
            }
            bouncer.onMove { x, y ->
                drawBitmap(x, y)
            }
        }

        private fun removeCallbacks() {
            bouncer.onChangeDirection { }
            bouncer.onMove { x, y -> }
        }

    }

}
