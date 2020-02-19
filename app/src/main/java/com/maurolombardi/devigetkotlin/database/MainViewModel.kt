package com.maurolombardi.devigetkotlin.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.maurolombardi.devigetkotlin.dto.dtos
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RedditRepo

    val allReddits: LiveData<List<dtos.Reddit>>

    init {
        val redditDao = RedditDB.getDatabase(application).redditDAO()
        repository = RedditRepo(redditDao)
        allReddits = repository.getReddits()
    }

    fun insertReddits(reddits: ArrayList<dtos.Reddit>) = viewModelScope.launch {
        repository.insertAll(reddits)
    }

    fun deleteReddit(reddit: dtos.Reddit)= viewModelScope.launch {
        repository.deleteReddit(reddit)
    }

    fun getPost(id: String): LiveData<dtos.Reddit> {
        return repository.getReddit(id)
    }
}