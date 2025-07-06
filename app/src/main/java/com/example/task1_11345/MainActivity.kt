package com.example.task1_11345

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task1_11345.fragments.EndGameFragment
import com.example.task1_11345.logic.GameManager
import com.example.task1_11345.utilities.SignalManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Timer
import java.util.TimerTask
import com.example.task1_11345.utilities.SingleSoundPlayer

class MainActivity : AppCompatActivity(){

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_IMG_spaceships: Array<AppCompatImageView>

    private lateinit var main_IMG_rocks: Array<Array<AppCompatImageView>>

    private lateinit var main_FAB_left: FloatingActionButton

    private lateinit var main_FAB_right: FloatingActionButton

    private lateinit var main_TEXT_score: TextView

    private lateinit var rockTimer: Timer

    private lateinit var scoreTimer: Timer

    private var rockTimerStarted = false

    private var scoreTimerStarted = false

    private var useSensors: Boolean = false

    private lateinit var gameManager: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useSensors = intent.getBooleanExtra("USE_SENSORS", false)
        setContentView(R.layout.activity_main)

        SignalManager.init(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        gameManager = GameManager(this, main_IMG_hearts.size)
        initViews()

        gameManager.onGameOver = {
            runOnUiThread {
                stopTimers()
                val intent = Intent(this, EndGameFragment::class.java)
                startActivity(intent)
                finish()
            }
        }

        gameManager.startGame(useSensors)
        startRocksTimer()
        startScoreTimer()
    }

    private fun findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left)
        main_FAB_right = findViewById(R.id.main_FAB_right)

        main_TEXT_score = findViewById(R.id.main_TEXT_score)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        main_IMG_spaceships = arrayOf(
            findViewById(R.id.main_IMG_spaceship0),
            findViewById(R.id.main_IMG_spaceship1),
            findViewById(R.id.main_IMG_spaceship2),
            findViewById(R.id.main_IMG_spaceship3),
            findViewById(R.id.main_IMG_spaceship4)
        )

        main_IMG_rocks = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_rock00),
                findViewById(R.id.main_IMG_rock01),
                findViewById(R.id.main_IMG_rock02),
                findViewById(R.id.main_IMG_rock03),
                findViewById(R.id.main_IMG_rock04)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock10),
                findViewById(R.id.main_IMG_rock11),
                findViewById(R.id.main_IMG_rock12),
                findViewById(R.id.main_IMG_rock13),
                findViewById(R.id.main_IMG_rock14)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock20),
                findViewById(R.id.main_IMG_rock21),
                findViewById(R.id.main_IMG_rock22),
                findViewById(R.id.main_IMG_rock23),
                findViewById(R.id.main_IMG_rock24)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock30),
                findViewById(R.id.main_IMG_rock31),
                findViewById(R.id.main_IMG_rock32),
                findViewById(R.id.main_IMG_rock33),
                findViewById(R.id.main_IMG_rock34)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock40),
                findViewById(R.id.main_IMG_rock41),
                findViewById(R.id.main_IMG_rock42),
                findViewById(R.id.main_IMG_rock43),
                findViewById(R.id.main_IMG_rock44)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock50),
                findViewById(R.id.main_IMG_rock51),
                findViewById(R.id.main_IMG_rock52),
                findViewById(R.id.main_IMG_rock53),
                findViewById(R.id.main_IMG_rock54)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock60),
                findViewById(R.id.main_IMG_rock61),
                findViewById(R.id.main_IMG_rock62),
                findViewById(R.id.main_IMG_rock63),
                findViewById(R.id.main_IMG_rock64)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock70),
                findViewById(R.id.main_IMG_rock71),
                findViewById(R.id.main_IMG_rock72),
                findViewById(R.id.main_IMG_rock73),
                findViewById(R.id.main_IMG_rock74)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_rock80),
                findViewById(R.id.main_IMG_rock81),
                findViewById(R.id.main_IMG_rock82),
                findViewById(R.id.main_IMG_rock83),
                findViewById(R.id.main_IMG_rock84)
            )
        )
    }

    private fun initViews() {
        if (useSensors) {
            main_FAB_right.visibility = View.INVISIBLE
            main_FAB_left.visibility = View.INVISIBLE

            gameManager.onPlayerMoved = {
                runOnUiThread {
                    refreshUI()
                }
            }

            gameManager.onDelayChanged = {
                restartRocksTimer()
            }
        }

        else {
            main_FAB_right.setOnClickListener { view: View -> move(1) }
            main_FAB_left.setOnClickListener { view: View -> move(-1) }
        }

        refreshUI()
    }

    private fun move(direction: Int) {
        gameManager.checkMove(direction)
        refreshUI()
    }

    private fun refreshUI() {
        main_TEXT_score.text = "${gameManager.score}"

        var hits: Int = gameManager.hits
        for (i in main_IMG_hearts.size - 1 downTo 0) {
            if (hits > 0) {
                main_IMG_hearts[i].visibility = View.INVISIBLE
                hits--
            }
            else {
                main_IMG_hearts[i].visibility = View.VISIBLE
            }
        }

        for (i in main_IMG_spaceships.indices) {
            if (i == gameManager.spaceshipPos) {
                main_IMG_spaceships[i].visibility = View.VISIBLE
            } else {
                main_IMG_spaceships[i].visibility = View.INVISIBLE
            }
        }

        for (i in 0 until gameManager.ROWS) {
            for (j in 0 until gameManager.COLS) {
                if (gameManager.rockBoard[i][j] == gameManager.ROCK) {
                    main_IMG_rocks[i][j].scaleType = ImageView.ScaleType.FIT_CENTER
                    main_IMG_rocks[i][j].setImageResource(R.drawable.rock)
                    main_IMG_rocks[i][j].visibility = View.VISIBLE
                }
                else if (gameManager.rockBoard[i][j] == gameManager.HEART){
                    main_IMG_rocks[i][j].scaleType = ImageView.ScaleType.FIT_CENTER
                    main_IMG_rocks[i][j].setImageResource(R.drawable.heart)
                    main_IMG_rocks[i][j].visibility = View.VISIBLE
                }
                else if (gameManager.rockBoard[i][j] == gameManager.COIN){
                    main_IMG_rocks[i][j].scaleType = ImageView.ScaleType.FIT_CENTER
                    main_IMG_rocks[i][j].setImageResource(R.drawable.coin)
                    main_IMG_rocks[i][j].visibility = View.VISIBLE
                }
                else {
                    main_IMG_rocks[i][j].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun startRocksTimer() {
        if (!rockTimerStarted) {
            rockTimerStarted = true
            rockTimer = Timer()
            rockTimer.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        game()
                    }
                }
            }, 0, gameManager.delay)
        }
    }

    private fun restartRocksTimer() {
        if (this::rockTimer.isInitialized) {
            rockTimer.cancel()
        }

        rockTimerStarted = false
        startRocksTimer()
    }


    private fun startScoreTimer() {
        if (!scoreTimerStarted) {
            scoreTimerStarted = true
            scoreTimer = Timer()
            scoreTimer.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        gameManager.gainScore(1)
                    }
                }
            }, 0, 1000)
        }
    }

    private fun game(){
        gameManager.rockFall()

        val hit: Int = gameManager.checkHit()
        if (hit > 0) { vibrateAndToast(hit) }
        refreshUI()

        if (gameManager.isGameOver) {
            reset()
        }
    }

    private fun vibrateAndToast(hit: Int){
        if (hit == gameManager.ROCK && !gameManager.isGameOver){
            SignalManager.getInstance().toast("collision! \uD83D\uDCA5")
            SingleSoundPlayer(this).playSound(R.raw.crash)
        }
        else if (hit == gameManager.HEART){
            SignalManager.getInstance().toast("You gain heart! ❤\uFE0F")
            SingleSoundPlayer(this).playSound(R.raw.heart)
        }
        else if (hit == gameManager.COIN){
            SignalManager.getInstance().toast("You gain points! \uD83D\uDCB0")
            SingleSoundPlayer(this).playSound(R.raw.score)
        }
        else{
            SignalManager.getInstance().toast("Game over! ☠\uFE0F")
            SingleSoundPlayer(this).playSound(R.raw.gameover)
        }

        SignalManager.getInstance().vibrate()
    }

    private fun reset(){
        stopTimers()

        gameManager.getCurrentLocationAndSaveHighScore(gameManager.score)

        vibrateAndToast(0)

        gameManager.resetGame()

        for (i in main_IMG_hearts.indices){
            main_IMG_hearts[i].visibility = View.VISIBLE
        }
    }

    private fun stopTimers() {
        if (this::rockTimer.isInitialized) {
            rockTimer.cancel()
        }

        if (this::scoreTimer.isInitialized) {
            scoreTimer.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimers()
    }
}