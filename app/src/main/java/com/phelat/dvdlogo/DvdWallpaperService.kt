package com.phelat.dvdlogo

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import kotlin.random.Random

class DvdWallpaperService : WallpaperService() {

    private lateinit var context: Context

    private var isVisible = false

    private val bitmap by lazy(LazyThreadSafetyMode.NONE) {
        BitmapFactory.decodeResource(context.resources, R.drawable.dvd)
    }

    private lateinit var dvdEntityX: DvdLogoState
    private lateinit var dvdEntityY: DvdLogoState

    override fun onCreateEngine(): Engine {
        context = this
        context.resources.displayMetrics.apply {
            val canvasSafeAreaForX = widthPixels - bitmap.width
            val canvasSafeAreaForY = heightPixels - bitmap.height

            val bitmapInitialX = Random.nextInt(bitmap.width, canvasSafeAreaForX)
            val bitmapInitialY = Random.nextInt(bitmap.height, canvasSafeAreaForY)

            dvdEntityX = DvdLogoState(bitmapInitialX, bitmap.width, widthPixels, 10)
            dvdEntityY = DvdLogoState(bitmapInitialY, bitmap.height, heightPixels, 10)
        }
        return DvdLogoEngine()
    }

    inner class DvdLogoEngine : Engine() {

        private var canvas: Canvas? = null

        private val handler = Handler()

        private val runnable = Runnable {
            initCanvas()
        }

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
                randomColor(),
                PorterDuff.Mode.SRC_IN
            )
        }

        private fun randomColor(): Int {
            return Color.argb(
                Random.nextInt(128, 256),
                Random.nextInt(50, 256),
                Random.nextInt(50, 256),
                Random.nextInt(50, 256)
            )
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            initCanvas()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            this@DvdWallpaperService.isVisible = visible
            if (visible) {
                initCanvas()
            } else {
                handler.removeCallbacks(runnable)
            }
        }

        private fun initCanvas() {
            canvas = null
            try {
                canvas = surfaceHolder.lockCanvas()
                if (canvas != null) {
                    draw()
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
            handler.removeCallbacks(runnable)
            if (isVisible) {
                handler.postDelayed(runnable, 32)
            }
        }

        private fun draw() {
            dvdEntityX.apply {
                dimensionInSafeArea += movementSpeed
                checkBorder(this)
            }
            dvdEntityY.apply {
                dimensionInSafeArea += movementSpeed
                checkBorder(this)
            }
            canvas?.apply {
                drawColor(Color.parseColor("#000000"))
                drawBitmap(
                    bitmap,
                    dvdEntityX.dimensionInSafeArea.toFloat(),
                    dvdEntityY.dimensionInSafeArea.toFloat(),
                    paint
                )
            }
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
                setPaintColor()
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            this@DvdWallpaperService.isVisible = false
            handler.removeCallbacks(runnable)
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(runnable)
        }

    }

}
