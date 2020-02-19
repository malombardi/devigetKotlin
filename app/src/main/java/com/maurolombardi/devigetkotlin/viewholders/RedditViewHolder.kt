package com.maurolombardi.devigetkotlin.viewholders

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.maurolombardi.devigetkotlin.R
import com.maurolombardi.devigetkotlin.adapter.RedditAdapter
import com.maurolombardi.devigetkotlin.dto.dtos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_viewholder_layout.view.*

import java.util.concurrent.TimeUnit

open class RedditViewHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val SIXTY = 60
    private val A_MONTH = 30
    private val A_YEAR = 12
    private val A_DAY_IN_HOURS = 24
    private val MINUTES = "minutes"
    private val HOURS = "hours"
    private val DAYS = "days"
    private val MONTHS = "months"
    private val YEARS = "years"

    private var res: Resources = context.resources

    fun setData(reddit: dtos.Reddit, listener: RedditAdapter.ItemClick) {

        itemView.item_title.text = (reddit.title)
        Picasso.with(context)
            .load(reddit.thumbnail)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(itemView.item_thumbnail)

        if (reddit.clicked) {
            itemView.item_clicked.visibility = View.GONE
        } else {
            itemView.item_clicked.visibility = View.VISIBLE
        }

        itemView.item_time.text = convertToDate(reddit.created)

        itemView.item_comments.text = res.getString(R.string.item_comments, reddit.numComments)

        itemView.item_thumbnail.setOnClickListener {
            listener.onThumbnailClicked(adapterPosition)
        }


        itemView.item_delete.setOnClickListener {
            listener.onDeleteClicked(adapterPosition)
        }
    }

    /**
     * This will return a text with the millis converted
     * x minutes ago
     * x hours ago
     * x days ago
     * x months ago
     * x years ago
     *
     * @param itemTime the seconds to convert
     * @return a string
     */
    private fun convertToDate(itemTime: Long): String {
        val currentMillis = System.currentTimeMillis()
        val itemMillis = TimeUnit.SECONDS.toMillis(itemTime)
        val diffTime = currentMillis - itemMillis

        if (itemTime > SIXTY) {
            val timeInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffTime)
            if (timeInMinutes > SIXTY) {
                val timeInHours = TimeUnit.MILLISECONDS.toHours(diffTime)
                if (timeInHours > A_DAY_IN_HOURS) {
                    val timeInDays = TimeUnit.MILLISECONDS.toDays(diffTime)
                    if (timeInDays > A_MONTH) {
                        val timeInMonths = timeInDays / A_MONTH
                        if (timeInMonths > A_YEAR) {
                            val timeInYears = timeInMonths / A_YEAR
                            return res.getString(R.string.item_time, timeInYears, YEARS)
                        } else {
                            return res.getString(R.string.item_time, timeInMonths, MONTHS)
                        }
                    } else {
                        return res.getString(R.string.item_time, timeInDays, DAYS)
                    }
                } else {
                    return res.getString(R.string.item_time, timeInHours, HOURS)
                }
            } else {
                return res.getString(R.string.item_time, timeInMinutes, MINUTES)
            }
        } else {
            return res.getString(R.string.item_moment)
        }
    }
}