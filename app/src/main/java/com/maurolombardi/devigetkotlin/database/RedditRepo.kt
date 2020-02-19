package com.maurolombardi.devigetkotlin.database

import androidx.lifecycle.LiveData
import com.maurolombardi.devigetkotlin.dto.dtos
import java.util.*

class RedditRepo(private val redditDao: RedditDAO) {

    fun getReddits() : LiveData<List<dtos.Reddit>>{
        return redditDao.getReddits()
    }

    fun getReddit(id: String): LiveData<dtos.Reddit> {
        return redditDao.getReddit(id)
    }

    fun insertAll(reddits: ArrayList<dtos.Reddit>) {
        redditDao.insertReddits(reddits)
    }

    fun deleteReddit(reddit: dtos.Reddit) {
        redditDao.deleteReddit(reddit)
    }

}