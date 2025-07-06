package com.example.task1_11345.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1_11345.MenuActivity
import com.example.task1_11345.R
import com.example.task1_11345.interfaces.Callback_HighScoreItemClicked
import com.example.task1_11345.logic.HighScoresManager
import com.example.task1_11345.logic.ScoreManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton


class HighScoresFragment : Fragment() {

    private lateinit var menu_BTN_back: MaterialButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView

    private val scoreManager = HighScoresManager()
    private var callback: Callback_HighScoreItemClicked? = null
    fun setCallback(callback: Callback_HighScoreItemClicked) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        val view = inflater.inflate(R.layout.high_scores_fragment, container, false)
        findViews(view)
        initViews()
        setupRecyclerView()
        return view
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }


    private fun findViews(view: android.view.View) {
        menu_BTN_back = view.findViewById(R.id.menu_BTN_back)
        recyclerView = view.findViewById(R.id.high_scores_recycler)
    }

    private fun initViews() {
        menu_BTN_back.setOnClickListener {
            activity?.let {
                val intent = Intent(it, MenuActivity::class.java)
                it.startActivity(intent)
            }

        }
    }

    private fun setupRecyclerView() {
        val scores = scoreManager.loadHighScores(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = ScoreManager(scores, callback ?: object : Callback_HighScoreItemClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
            }
        })
    }

}