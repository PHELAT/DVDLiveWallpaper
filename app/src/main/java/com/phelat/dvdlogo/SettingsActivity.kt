package com.phelat.dvdlogo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPreference = getSharedPreferences("DVD_OPTIONS", Context.MODE_PRIVATE)

        val movementSpeedOptions = resources.getStringArray(R.array.movement_speed_options)

        val savedSpeed = sharedPreference.getInt("MovementSpeed", 32)
        when (savedSpeed) {
            movementSpeedOptions[0].toInt() -> movementSpeedOption0.isSelected = true
            movementSpeedOptions[1].toInt() -> movementSpeedOption1.isSelected = true
            movementSpeedOptions[2].toInt() -> movementSpeedOption2.isSelected = true
        }

        movementSpeedOption0.apply {
            text = movementSpeedOptions[0]
            setOnClickListener {
                isSelected = true
                movementSpeedOption1.isSelected = false
                movementSpeedOption2.isSelected = false
                sharedPreference.edit()
                    .putInt("MovementSpeed", movementSpeedOptions[0].toInt())
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
                    .putInt("MovementSpeed", movementSpeedOptions[1].toInt())
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
                    .putInt("MovementSpeed", movementSpeedOptions[2].toInt())
                    .apply()
            }
        }
    }

}
