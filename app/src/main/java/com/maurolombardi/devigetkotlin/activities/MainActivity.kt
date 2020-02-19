package com.maurolombardi.devigetkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.maurolombardi.devigetkotlin.DevigetApplication
import com.maurolombardi.devigetkotlin.R
import com.maurolombardi.devigetkotlin.adapter.RedditAdapter
import com.maurolombardi.devigetkotlin.database.DBExcecutor
import com.maurolombardi.devigetkotlin.database.MainViewModel
import com.maurolombardi.devigetkotlin.database.RedditDB
import com.maurolombardi.devigetkotlin.dto.dtos
import com.maurolombardi.devigetkotlin.dto.dtos.Reddit
import com.maurolombardi.devigetkotlin.fragments.DetailFragment
import kotlinx.android.synthetic.main.empty_layout.*
import kotlinx.android.synthetic.main.main_activity_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

open class MainActivity : AppCompatActivity(), RedditAdapter.ItemClick {

    companion object {
        const val INTENT_REDDIT = "intent_reddit"
    }

    //will be used to know if we are in a tablet or a phone
    private var isTwoPane: Boolean = false

    private var posts: ArrayList<Reddit> = ArrayList<Reddit>()

    private var adapter: RedditAdapter? = null

    private var redditsCall: Call<dtos.RedditJsonResponse>? = null

    private lateinit var redditDB: RedditDB

    private lateinit var excecutor: DBExcecutor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)

        excecutor = DBExcecutor("dbexcecutor")
        excecutor.start()

        redditDB = RedditDB.getDatabase(context = this)

        item_detail_container?.let { isTwoPane = true }

        swipeRefreshLayout!!.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent)

        swipeRefreshLayout.setOnRefreshListener {
            showLoading()
            loadDataFromWeb()
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = RedditAdapter(this)
        recyclerView.adapter = adapter

        redditsCall = DevigetApplication.getApiInterface().getReddits()
        loadDataFromDB()

    }

    override fun onPause() {
        super.onPause()
        if (redditsCall!!.isExecuted()) {
            redditsCall = null
        }
    }

    override fun onResume() {
        super.onResume()
        showLoading()
        if (redditsCall == null) {
            redditsCall = DevigetApplication.getApiInterface().getReddits()
        }
        loadDataFromWeb()
    }

    private fun loadDataFromWeb() {
        if (!redditsCall!!.isExecuted()) {
            redditsCall!!.enqueue(object : Callback<dtos.RedditJsonResponse> {
                override fun onResponse(
                    call: Call<dtos.RedditJsonResponse>,
                    response: Response<dtos.RedditJsonResponse>
                ) {
                    val children = response.body()!!.data.children
                    for (child in children) {
                        if (!posts.contains(child.data)) {
                            posts.add(child.data)
                        }
                    }

                    val task = Runnable { redditDB.redditDAO().insertReddits(posts) }
                    excecutor.postTask(task)
                    refreshUI()
                }

                override fun onFailure(
                    call: Call<dtos.RedditJsonResponse>,
                    t: Throwable
                ) {
                    //TODO show some error layout
                }
            })
        } else {
            refreshUI()
        }
    }

    private fun loadDataFromDB() {
        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.allReddits.observe(this, Observer { latestsPosts ->
            posts = ArrayList(latestsPosts)
            refreshUI()
        })
    }

    private fun showLoading() {
        progress!!.visibility = View.VISIBLE
        empty_layout!!.visibility = View.GONE
    }

    private fun refreshUI() {
        progress!!.visibility = View.GONE
        empty_layout!!.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
        swipeRefreshLayout!!.isRefreshing = false
        adapter!!.setItems(posts)
        adapter!!.notifyDataSetChanged()
    }

    override fun onThumbnailClicked(position: Int) {
        val selectedReddit = posts[position]
        updateReddit(position)
        if (!isTwoPane) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(INTENT_REDDIT, selectedReddit.id)
            startActivity(intent)
        } else {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val detailFragment = DetailFragment()
            detailFragment.setSelectedReddit(selectedReddit)

            fragmentTransaction.replace(R.id.item_detail_container, detailFragment)
            fragmentTransaction.commit()
        }
    }

    private fun updateReddit(position: Int) {
        val redditToUpdate = posts[position]
        redditToUpdate.clicked = (true)
        adapter!!.notifyItemChanged(position)
        val task = Runnable { redditDB.redditDAO().updateReddit(redditToUpdate) }
        excecutor.postTask(task)
    }

    override fun onDeleteClicked(position: Int) {
        val itemView = recyclerView!!.findViewHolderForAdapterPosition(position)!!.itemView

        val anim = AnimationUtils.loadAnimation(
            this,
            android.R.anim.fade_out
        )
        anim.duration = 600
        itemView.startAnimation(anim)

        Handler().postDelayed({
            val redditToDelete = posts[position]
            val task = Runnable { redditDB.redditDAO().deleteReddit(redditToDelete) }
            excecutor.postTask(task)
        }, anim.duration)
    }
}