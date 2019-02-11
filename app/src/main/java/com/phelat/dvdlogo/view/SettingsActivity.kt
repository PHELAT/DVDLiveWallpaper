package com.phelat.dvdlogo.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.forEach
import com.phelat.dvdlogo.Bouncer
import com.phelat.dvdlogo.constant.OptionsConstant
import com.phelat.dvdlogo.R
import com.phelat.dvdlogo.provider.BouncerProvider
import com.phelat.dvdlogo.provider.ColorProvider
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val sharedPreference by lazy(LazyThreadSafetyMode.NONE) {
        getSharedPreferences(OptionsConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private lateinit var bouncer: Bouncer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        movementSpeedTitle.isSelected = true

        val movementSpeedOptions = mutableListOf<Int>()
        movementSpeedHolder.forEach { optionView ->
            val speed = (optionView.tag as String).toInt()
            initMovementSpeedOptionView(
                optionView as AppCompatTextView,
                speed
            )
            movementSpeedOptions += speed
        }
        val savedSpeed = fetchSavedMovementSpeed(movementSpeedOptions)

        bouncer = BouncerProvider().provide(
            resources.displayMetrics,
            BitmapFactory.decodeResource(resources, R.drawable.dvd)
        ).apply {
            speed = savedSpeed
            onMove { _, _ ->
                bouncer.isRunning = true
            }
            onChangeDirection {
                changeColor()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bouncer.isRunning = true
    }

    private fun changeColor() {
        dvdLogo.colorFilter = PorterDuffColorFilter(
            ColorProvider.provide(),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun fetchSavedMovementSpeed(movementSpeedOptions: MutableList<Int>): Int {
        val savedSpeed = sharedPreference.getInt(
            OptionsConstant.MOVEMENT_SPEED_OPTION,
            OptionsConstant.MOVEMENT_SPEED_DEFAULT
        )
        when (savedSpeed) {
            movementSpeedOptions[0] -> movementSpeedOption0.isSelected = true
            movementSpeedOptions[1] -> movementSpeedOption1.isSelected = true
            movementSpeedOptions[2] -> movementSpeedOption2.isSelected = true
            movementSpeedOptions[3] -> movementSpeedOption3.isSelected = true
        }
        return savedSpeed
    }

    private fun initMovementSpeedOptionView(view: AppCompatTextView, movementSpeed: Int) {
        view.setOnClickListener {
            it.isSelected = true
            movementSpeedHolder.forEach { child ->
                if (child.id != it.id) {
                    child.isSelected = false
                }
            }
            setMovementSpeed(movementSpeed)
        }
    }

    private fun setMovementSpeed(speed: Int) {
        bouncer.speed = speed
        sharedPreference.edit()
            .putInt(OptionsConstant.MOVEMENT_SPEED_OPTION, speed)
            .apply()
    }

    override fun onPause() {
        super.onPause()
        bouncer.isRunning = false
    }

}
