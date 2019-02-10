package com.phelat.dvdlogo

import android.content.Context
import android.service.wallpaper.WallpaperService

class DvdLogoService : WallpaperService() {

    private lateinit var context: Context

    override fun onCreateEngine(): Engine {
        context = this
        TODO("Implement engine")
    }

}
