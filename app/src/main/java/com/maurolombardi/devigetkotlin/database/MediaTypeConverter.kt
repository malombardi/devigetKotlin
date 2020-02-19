package com.maurolombardi.devigetkotlin.database

import androidx.room.TypeConverter
import com.maurolombardi.devigetkotlin.dto.dtos
import com.maurolombardi.devigetkotlin.dto.dtos.OTHER_TAG
import com.maurolombardi.devigetkotlin.dto.dtos.SEPARATOR_TAG
import com.maurolombardi.devigetkotlin.dto.dtos.VIDEO_TAG

open class MediaTypeConverter {
    @TypeConverter
    fun toMedia(mediaString: String?): dtos.Media? {
        if (mediaString == null) {
            return null
        }
        val media = dtos.Media()

        if (mediaString.contains(VIDEO_TAG)) {
            val videoData =
                mediaString.split(VIDEO_TAG.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split(
                    SEPARATOR_TAG.toRegex()
                ).dropLastWhile { it.isEmpty() }.toTypedArray()
            val video = dtos.Video()
            video.url = videoData[0]
            video.height = Integer.valueOf(videoData[1])
            video.width = Integer.valueOf(videoData[2])
            media.redditVideo = video
        }

        if (mediaString.contains(OTHER_TAG)) {
            val otherData =
                mediaString.split(OTHER_TAG.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split(
                    SEPARATOR_TAG.toRegex()
                ).dropLastWhile { it.isEmpty() }.toTypedArray()
            val other = dtos.Other()
            other.url = otherData[0]
            other.height = Integer.valueOf(otherData[1])
            other.width = Integer.valueOf(otherData[2])
            other.description = otherData[3]
            other.title = otherData[4]
            media.other = other
        }

        return media
    }

    @TypeConverter
    fun fromMediaToString(media: dtos.Media?): String? {
        media?.let {
            it.redditVideo?.let { it ->
                return it.toString()
            }
            it.other?.let { it ->
                return it.toString()
            }
        }
        return null
    }
}