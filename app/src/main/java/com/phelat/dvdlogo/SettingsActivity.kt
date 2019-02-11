package com.phelat.dvdlogo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val sharedPreference by lazy(LazyThreadSafetyMode.NONE) {
        getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val movementSpeedOptions = resources.getStringArray(R.array.movement_speed_options)

        fetchSavedMovementSpeed(movementSpeedOptions)

        movementSpeedOption0.apply {
            text = movementSpeedOptions[0]
            setOnClickListener {
                isSelected = true
                movementSpeedOption1.isSelected = false
                movementSpeedOption2.isSelected = false
                sharedPreference.edit()
                    .putInt(MOVEMENT_SPEED_OPTION, movementSpeedOptions[0].toInt())
                    .apply()
            }
        }
        movementSpeedOption1.apply {
            text = movementSpeedOptions[1]
            setOnClickListener {
                isSelected = true
                movementSpeedOption0.isSelected = false
                movementSpeedOption2.isSelected = false
                sharedPreference.edit()
                    .putInt(MOVEMENT_SPEED_OPTION, movementSpeedOptions[1].toInt())
                    .apply()
            }
        }
        movementSpeedOption2.apply {
            text = movementSpeedOptions[2]
            setOnClickListener {
                isSelected = true
                movementSpeedOption0.isSelected = false
                movementSpeedOption1.isSelected = false
                sharedPreference.edit()
                    .putInt(MOVEMENT_SPEED_OPTION, movementSpeedOptions[2].toInt())
                    .apply()
            }
        }
    }

    private fun fetchSavedMovementSpeed(movementSpeedOptions: Array<String>) {
        val savedSpeed = sharedPreference.getInt(MOVEMENT_SPEED_OPTION, MOVEMENT_SPEED_DEFAULT)
        when (savedSpeed) {
            movementSpeedOptions[0].toInt() -> movementSpeedOption0.isSelected = true
            movementSpeedOptions[1].toInt() -> movementSpeedOption1.isSelected = true
            movementSpeedOptions[2].toInt() -> movementSpeedOption2.isSelected = true
        }
    }

    companion object {
        const val MOVEMENT_SPEED_OPTION = "MovementSpeed"
        const val SHARED_PREF_NAME = "DVD_OPTIONS"
        const val MOVEMENT_SPEED_DEFAULT = 32
    }

}
