package com.maurolombardi.devigetkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.maurolombardi.devigetkotlin.dto.dtos

@Dao
interface RedditDAO {

    @Query("SELECT * FROM posts")
    fun getReddits(): LiveData<List<dtos.Reddit>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    fun getReddit(id: String): LiveData<dtos.Reddit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReddits(reddits: List<dtos.Reddit>)

    @Delete
    fun deleteReddit(reddit: dtos.Reddit)

    @Update
    fun updateReddit(reddit: dtos.Reddit)

}