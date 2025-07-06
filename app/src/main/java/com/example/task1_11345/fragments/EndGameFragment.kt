package com.example.task1_11345.fragments

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task1_11345.R
import com.example.task1_11345.interfaces.Callback_HighScoreItemClicked

class EndGameFragment: AppCompatActivity() {
    private lateinit var main_FRAME_highScores: FrameLayout
    private lateinit var main_FRAME_map: FrameLayout

    private lateinit var mapFragment: MapFragment
    private lateinit var highScoresFragment: HighScoresFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.end_game_fragment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()

        supportFragmentManager
            .beginTransaction()
            .detach(highScoresFragment)
            .attach(highScoresFragment)
            .commit()

    }

    private fun findViews() {
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
        main_FRAME_highScores = findViewById(R.id.main_FRAME_highScores)
    }

    private fun initViews() {
        mapFragment = MapFragment()
        highScoresFragment = HighScoresFragment()

        highScoresFragment.setCallback(object : Callback_HighScoreItemClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                mapFragment.moveToHighScoreLocation(lat, lon)
            }
        })

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_map, mapFragment)
            .commit()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_highScores, highScoresFragment)
            .commit()
    }
}