package com.example.task1_11345

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.task1_11345.fragments.EndGameFragment


class MenuActivity : AppCompatActivity() {

    private lateinit var menu_SWITCH_control: Switch
    private lateinit var menu_LBL_buttons: TextView
    private lateinit var menu_LBL_sensors: TextView
    private lateinit var menu_BTN_start: Button
    private lateinit var menu_BTN_scores: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        findViews()

        menu_BTN_start.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USE_SENSORS", menu_SWITCH_control.isChecked)
            startActivity(intent)
        }

        menu_BTN_scores.setOnClickListener {
            val intent = Intent(this, EndGameFragment::class.java)
            startActivity(intent)
        }
    }

    private fun findViews() {
        menu_SWITCH_control = findViewById(R.id.menu_SWITCH_control)
        menu_LBL_buttons = findViewById(R.id.menu_LBL_buttons)
        menu_LBL_sensors = findViewById(R.id.menu_LBL_sensors)
        menu_BTN_start = findViewById(R.id.menu_BTN_start)
        menu_BTN_scores = findViewById(R.id.menu_BTN_scores)
    }
}