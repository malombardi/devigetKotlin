package com.maurolombardi.devigetkotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maurolombardi.devigetkotlin.dto.dtos

@Database(entities = arrayOf(dtos.Reddit::class), version = 1, exportSchema = false)
@TypeConverters(MediaTypeConverter::class)
abstract class RedditDB : RoomDatabase() {

    abstract fun redditDAO(): RedditDAO

    companion object {

        private const val DATABASE_NAME = "redditdatabase"
        @Volatile
        private var INSTANCE: RedditDB? = null

        fun getDatabase(context: Context): RedditDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RedditDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }


}
