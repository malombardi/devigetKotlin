package com.maurolombardi.devigetkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maurolombardi.devigetkotlin.R
import com.maurolombardi.devigetkotlin.dto.dtos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_fragment_layout.*

open class DetailFragment : Fragment() {

    companion object {
        private const val PARCELABLE_REDDIT = "parcelable_reddit"
        private const val TAG = "DetailFragment"
    }

    private var reddit: dtos.Reddit? = null

    fun setSelectedReddit(reddit: dtos.Reddit) {
        this.reddit = reddit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            reddit = it.getParcelable(PARCELABLE_REDDIT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment_layout, container, false)

    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI() {
        reddit?.let {
            title!!.text = it.title
            Picasso.with(context)
                .load(it.thumbnail)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(image)

            val mediaUrl = getMediaURL()

            if (mediaUrl != null) {
                image!!.setOnClickListener { v ->
                    val dialogFragment = MediaFragmentDialog(mediaUrl)
                    dialogFragment.show(activity!!.supportFragmentManager, TAG)
                }
            } else {
                image!!.setOnClickListener { v ->
                    val dialogFragment = MediaFragmentDialog(reddit!!.thumbnail)
                    dialogFragment.show(activity!!.supportFragmentManager, TAG)
                }
            }
        }
    }

    private fun getMediaURL(): String? {
        reddit!!.media?.let {
            it.redditVideo?.let { it ->
                return it.url
            }
            it.other?.let { it ->
                return it.url
            }
        }
        return null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(PARCELABLE_REDDIT, reddit)
    }
}