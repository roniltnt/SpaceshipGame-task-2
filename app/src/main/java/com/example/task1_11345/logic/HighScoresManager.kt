package com.example.task1_11345.logic

import android.content.Context
import android.util.Log
import com.example.task1_11345.model.HighScore
import org.json.JSONArray
import org.json.JSONObject

class HighScoresManager {

    fun saveHighScores(context: Context, scores: List<HighScore>) {
        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val jsonArray = JSONArray()
        for (score in scores) {
            val obj = JSONObject()
            obj.put("score", score.score)
            obj.put("lat", score.latitude)
            obj.put("lon", score.longitude)
            jsonArray.put(obj)
        }

        val jsonString = jsonArray.toString()
        Log.d("HighScoreManager", "Saving scores: $jsonString")
        editor.putString("scores", jsonString)
        editor.apply()
    }

    fun loadHighScores(context: Context): List<HighScore> {
        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        val jsonString = prefs.getString("scores", null) ?: return emptyList()

        if (!jsonString.trim().startsWith("[")) {
            Log.e("HighScoreManager", "Invalid JSON array in high scores: $jsonString")
            clearHighScores(context)
            return emptyList()
        }
        val jsonArray = JSONArray(jsonString)
        val highScores = mutableListOf<HighScore>()
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.optJSONObject(i)
            if (jsonObj != null) {
                val score = jsonObj.optInt("score", 0)
                val lat = jsonObj.optDouble("lat", 0.0)
                val lon = jsonObj.optDouble("lon", 0.0)
                highScores.add(HighScore(score, lat, lon))
            } else {
                Log.e("HighScoreManager", "Skipped invalid item at index $i: ${jsonArray.get(i)}")
            }
        }
        return highScores
    }


    fun updateHighScores(context: Context, newScore: HighScore) {
        val currentScores = loadHighScores(context).toMutableList()
        currentScores.add(newScore)
        val sorted = currentScores.sortedByDescending { it.score }.take(10)
        saveHighScores(context, sorted)
    }

    fun clearHighScores(context: Context) {
        val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}