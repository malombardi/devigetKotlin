package com.maurolombardi.devigetkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maurolombardi.devigetkotlin.R
import com.maurolombardi.devigetkotlin.dto.dtos
import com.maurolombardi.devigetkotlin.viewholders.RedditViewHolder
import java.util.ArrayList

class RedditAdapter(private val listener: ItemClick) : RecyclerView.Adapter<RedditViewHolder>() {

    private var items: List<dtos.Reddit>? = null

    init {
        this.items = ArrayList<dtos.Reddit>()
    }

    fun setItems(items: List<dtos.Reddit>) {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val viewHolderView = inflater
            .inflate(R.layout.item_viewholder_layout, parent, false)

        return RedditViewHolder(context, viewHolderView)
    }

    override fun onBindViewHolder(holder: RedditViewHolder, position: Int) {
        val post = items!![position]

        holder.setData(post, listener)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    interface ItemClick {
        fun onThumbnailClicked(position: Int)

        fun onDeleteClicked(position: Int)
    }
}