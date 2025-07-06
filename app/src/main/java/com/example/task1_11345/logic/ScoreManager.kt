package com.example.task1_11345.logic

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1_11345.R
import com.example.task1_11345.interfaces.Callback_HighScoreItemClicked
import com.example.task1_11345.model.HighScore

class ScoreManager(
    private val scores: List<HighScore>,
    private val callback: Callback_HighScoreItemClicked
) : RecyclerView.Adapter<ScoreManager.ScoreViewHolder>() {

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeText: TextView = itemView.findViewById(R.id.score_line)
        val scoreText: TextView = itemView.findViewById(R.id.score_points)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.high_scores_table, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val highScore = scores[position]
        holder.placeText.text = (position + 1).toString()
        holder.scoreText.text = "  Score: ${highScore.score}"

        holder.itemView.setOnClickListener {
            callback.highScoreItemClicked(highScore.latitude, highScore.longitude)
            Log.d("HighScoresFragment", "Clicked location: ${highScore.latitude}, ${highScore.longitude}")

        }
    }

    override fun getItemCount(): Int = scores.size
}