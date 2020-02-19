package com.maurolombardi.devigetkotlin.dto

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

object dtos {

    val OTHER_TAG = "other: "
    val SEPARATOR_TAG = " : "
    val VIDEO_TAG = "video: "


    @Parcelize
    @Entity(tableName = "posts")
    data class Reddit(
        @NonNull
        @PrimaryKey(autoGenerate = false) var id: String,
        @SerializedName("author_fullname") var author: String?,
        var title: String?,
        var clicked: Boolean = false,
        var thumbnail: String?,
        var url: String?,
        @SerializedName("created_utc") var created: Long,
        var media: Media?,
        @SerializedName("num_comments") var numComments: Int?
    ) : Parcelable

    @Parcelize
    data class Media(
        @SerializedName("reddit_video") var redditVideo: Video? = null,
        @SerializedName("oembed") var other: Other? = null
    ) : Parcelable

    @Parcelize
    data class Video(
        @SerializedName("fallback_url") var url: String? = null,
        var height: Int = 0,
        var width: Int = 0
    ) : Parcelable {
        override fun toString(): String {
            return VIDEO_TAG + url + SEPARATOR_TAG + height + SEPARATOR_TAG + width
        }
    }

    @Parcelize
    data class Other(
        var description: String? = null,
        var title: String? = null,
        var height: Int = 0,
        var width: Int = 0,
        @SerializedName("thumbnail_url") var url: String? = null
    ) : Parcelable {
        override fun toString(): String {
            return (OTHER_TAG + url + SEPARATOR_TAG + height + SEPARATOR_TAG + width
                    + SEPARATOR_TAG + description + SEPARATOR_TAG + title)
        }
    }

    @Parcelize
    data class ChildrenData(var data: Reddit) : Parcelable

    @Parcelize
    data class RedditData(var children: List<ChildrenData>) : Parcelable

    @Parcelize
    data class RedditJsonResponse(var data: RedditData) : Parcelable

}