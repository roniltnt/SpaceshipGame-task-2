package com.example.task1_11345.logic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.task1_11345.interfaces.TiltCallBack
import com.example.task1_11345.model.HighScore
import com.example.task1_11345.model.Player
import com.example.task1_11345.utilities.TiltManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.random.Random


class GameManager(private val context: Context, private val lifeCount: Int = 3) : TiltCallBack {
    val ROCK: Int
        get() = 1

    val HEART: Int
        get() = 2

    val COIN: Int
        get() = 3

    val ROWS: Int
        get() = 9

    val COLS: Int
        get() = 5

    private val spaceshipBoard = Array(COLS) { 0 }

    var spaceshipPos = 2

    private var rockPos: Int = 0

    val rockBoard = Array(ROWS) { Array(COLS) { 0 } }

    init { spaceshipBoard[spaceshipPos] = 1 }

    var hits: Int = 0
        private set

    private val player = Player(lives = lifeCount - hits)

    val score: Int
        get() = player.score

    val isGameOver: Boolean
        get() = hits == lifeCount

    var onGameOver: (() -> Unit)? = null

    var onPlayerMoved: (() -> Unit)? = null

    var onDelayChanged: (() -> Unit)? = null

    private var sensorManager: TiltManager? = null

    private var lastSensorMoveTime: Long = 0

    private val sensorMoveCooldown = 300

    var delay: Long = 500
        private set

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private var scoreManager = HighScoresManager()

    fun checkMove(direction: Int) {
        spaceshipBoard[spaceshipPos] = 0

        if ((direction == -1 && spaceshipPos > 0) || (direction ==  1 && spaceshipPos < COLS - 1)) {
            spaceshipPos += direction
        }

        spaceshipBoard[spaceshipPos] = 1
    }

    fun startGame(useSensors : Boolean) {
        if (useSensors) {
            sensorManager = TiltManager(context, this)
            sensorManager?.start()
        }
    }

    fun rockFall() {
        for (i in (ROWS - 1) downTo 1){
            for (j in 0 until COLS){
                rockBoard[i][j] = rockBoard[i - 1][j]
            }
        }

        for (i in 0 until COLS)
            rockBoard[0][i] = 0

        rockPos = Random.nextInt(0, COLS)

        rockBoard[0][rockPos] = ROCK

        heartFall()
        coinFall()
    }

    private fun heartFall() {
        if (player.lives >= 3) {
            return
        }

        val fall = (0..4).random() == 0
        if (fall) {
            rockBoard[0][rockPos] = HEART
        }
    }

    private fun coinFall() {
        val fall = (0..8).random() == 0
        if (fall) {
            rockBoard[0][rockPos] = COIN
        }
    }

    fun checkHit(): Int {
        for (i in 0 until COLS){
            if (rockBoard[ROWS - 1][i] > 0) {
                rockPos = i
                break
            }
        }

        if (spaceshipPos == rockPos){
            if (rockBoard[ROWS - 1][rockPos] == ROCK){
                hits++
                player.lives--
                return ROCK
            }
            else if (rockBoard[ROWS - 1][rockPos] == HEART){
                if (player.lives < 3){
                    player.lives++
                    hits--
                    return HEART
                }
            }
            else if (rockBoard[ROWS - 1][rockPos] == COIN){
                gainScore(30)
                return COIN
            }
        }

        return 0
    }

    fun gainScore(points: Int) {
        player.score += points
    }

    fun resetGame(){
        for (i in 0 until ROWS){
            for (j in 0 until COLS){
                rockBoard[i][j] = 0
            }
        }

        for (i in 0 until COLS){
            spaceshipBoard[i] = 0
        }
        spaceshipPos = COLS / 2
        spaceshipBoard[spaceshipPos] = 1

        hits = 0
        player.lives = 3
        player.score = 0

        sensorManager?.stop()

    }


    fun getCurrentLocationAndSaveHighScore(scoreToSave: Int) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            saveHighScoreWithoutLocation(scoreToSave)
            onGameOver?.invoke()
            return
        }

        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            val lat = location?.latitude ?: 0.0
            val lon = location?.longitude ?: 0.0
            val newHighScore = HighScore(scoreToSave, lat, lon)
            scoreManager.updateHighScores(context, newHighScore)
            onGameOver?.invoke()
        }.addOnFailureListener {
            saveHighScoreWithoutLocation(scoreToSave)
            onGameOver?.invoke()
        }
    }

    private fun saveHighScoreWithoutLocation(scoreToSave: Int) {
        val newHighScore = HighScore(scoreToSave, 0.0, 0.0)
        scoreManager.updateHighScores(context, newHighScore)
    }

    override fun moveBySensor(x: Float) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastSensorMoveTime < sensorMoveCooldown) return

        if (x >= 2.0) {
            checkMove(-1)
            onPlayerMoved?.invoke()
        } else if (x <= -2.0) {
            checkMove(1)
            onPlayerMoved?.invoke()
        }

        lastSensorMoveTime = currentTime
    }

    override fun changeRockSpeed(y: Float) {
        val maxTilt = 5.0f
        val minDelay = 300L
        val maxDelay = 1500L
        val clampedTilt = y.coerceIn(0f, maxTilt)
        delay = ((1 - (clampedTilt / maxTilt)) * (maxDelay - minDelay) + minDelay).toLong()

        onDelayChanged?.invoke()
    }

}