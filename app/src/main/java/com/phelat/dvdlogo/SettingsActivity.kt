package com.phelat.dvdlogo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.forEach
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val sharedPreference by lazy(LazyThreadSafetyMode.NONE) {
        getSharedPreferences(OptionsConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

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
        fetchSavedMovementSpeed(movementSpeedOptions)
    }

    private fun fetchSavedMovementSpeed(movementSpeedOptions: MutableList<Int>) {
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
        sharedPreference.edit()
            .putInt(OptionsConstant.MOVEMENT_SPEED_OPTION, speed)
            .apply()
    }

}
