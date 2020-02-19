package com.maurolombardi.devigetkotlin.activities

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maurolombardi.devigetkotlin.R
import com.maurolombardi.devigetkotlin.activities.MainActivity.Companion.INTENT_REDDIT
import com.maurolombardi.devigetkotlin.database.MainViewModel
import com.maurolombardi.devigetkotlin.dto.dtos
import com.maurolombardi.devigetkotlin.fragments.DetailFragment

open class DetailActivity : AppCompatActivity() {

    private var redditID: String? = null
    private var reddit: dtos.Reddit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity_layout)
        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
            onBackPressed()
        }
        if (savedInstanceState != null) {
            redditID = savedInstanceState.getString(INTENT_REDDIT)
        } else {
            redditID = intent.getStringExtra(INTENT_REDDIT)
        }

        loadDataFromDB()

    }

    private fun loadDataFromDB() {
        redditID?.let {
            val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            mainViewModel.getPost(redditID!!).observe(this, Observer { post ->
                reddit = post
                refreshUI()
            })
        }
    }

    private fun refreshUI() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val detailFragment = DetailFragment()
        detailFragment.setSelectedReddit(reddit!!)

        fragmentTransaction.replace(R.id.detail_fragment, detailFragment)
        fragmentTransaction.commit()
    }

}