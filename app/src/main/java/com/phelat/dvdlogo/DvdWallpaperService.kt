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

    override fun onCreateEngine(): Engine {
        context = this
        return DvdLogoEngine()
    }

    inner class DvdLogoEngine : Engine() {

        private var canvas: Canvas? = null

        private val bitmap by lazy(LazyThreadSafetyMode.NONE) {
            BitmapFactory.decodeResource(context.resources, R.drawable.dvd)
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

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            initCanvas()
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
        }

        private fun draw() {
            canvas?.apply {
                drawColor(Color.parseColor("#000000"))
                drawBitmap(
                    bitmap,
                    (width - bitmap.width).toFloat() / 2,
                    (height - bitmap.height).toFloat() / 2,
                    paint
                )
            }
        }

    }

}
