package com.example.profilelab.di

import com.example.profilelab.models.NotificationModel
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiInterface {

    companion object {
        const val BASE_URL = "https://fcm.googleapis.com/"
    }

    @Headers(
        "Authorization: key=AAAA1e2NnyY:APA91bEkxKVpplia3i3icjwOTejxAPooi9xSEyQ54SMzyhZLypNfMTjd5u1aWPkM9_EG4vVeMy06zrZOM5AdQgeEUmRajVfGWAaBV3QZzlxasK1sYyhFTAYRECv3sE_8VjFuuUKrwiU1"
        ,
        "Content-Type:application/json"
    )

    @POST("fcm/send")
    suspend fun sendNotification( @Body notificationModel: NotificationModel): Response<ResponseBody>



}