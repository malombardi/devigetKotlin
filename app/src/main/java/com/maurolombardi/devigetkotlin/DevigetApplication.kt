package com.maurolombardi.devigetkotlin

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.maurolombardi.devigetkotlin.database.RedditDB
import com.maurolombardi.devigetkotlin.interfaces.APIInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class DevigetApplication : Application() {
    companion object {
        private const val BASE_URL = "https://www.reddit.com/"

        private val GSON = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()

        fun getApiInterface(): APIInterface {
            val client = OkHttpClient.Builder()
            client.connectTimeout(5, TimeUnit.SECONDS)
            client.readTimeout(5, TimeUnit.SECONDS)
            client.writeTimeout(5, TimeUnit.SECONDS)

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .client(client.build())
                .build()
                .create<APIInterface>(APIInterface::class.java)
        }
    }
}