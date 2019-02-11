package com.phelat.dvdlogo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val sharedPreference by lazy(LazyThreadSafetyMode.NONE) {
        getSharedPreferences(OptionsConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val movementSpeedOptions = resources.getStringArray(R.array.movement_speed_options)

        fetchSavedMovementSpeed(movementSpeedOptions)

        movementSpeedHolder.forEachIndexed { index, optionView ->
            initMovementSpeedOptionView(
                optionView as AppCompatTextView,
                index,
                movementSpeedOptions
            )
        }
    }

    private fun fetchSavedMovementSpeed(movementSpeedOptions: Array<String>) {
        val savedSpeed = sharedPreference.getInt(
            OptionsConstant.MOVEMENT_SPEED_OPTION,
            OptionsConstant.MOVEMENT_SPEED_DEFAULT
        )
        when (savedSpeed) {
            movementSpeedOptions[0].toInt() -> movementSpeedOption0.isSelected = true
            movementSpeedOptions[1].toInt() -> movementSpeedOption1.isSelected = true
            movementSpeedOptions[2].toInt() -> movementSpeedOption2.isSelected = true
        }
    }

    private fun initMovementSpeedOptionView(
        view: AppCompatTextView,
        position: Int,
        movementSpeedOptions: Array<String>
    ) {
        view.apply {
            text = movementSpeedOptions[position]
            setOnClickListener {
                isSelected = true
                movementSpeedHolder.forEach { child ->
                    if (child.id != view.id) {
                        child.isSelected = false
                    }
                }
                setMovementSpeed(movementSpeedOptions[position].toInt())
            }
        }
    }

    private fun setMovementSpeed(speed: Int) {
        sharedPreference.edit()
            .putInt(OptionsConstant.MOVEMENT_SPEED_OPTION, speed)
            .apply()
    }

}
