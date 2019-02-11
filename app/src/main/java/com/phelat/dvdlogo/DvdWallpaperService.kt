package com.phelat.dvdlogo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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
            }
        }

    }

}
