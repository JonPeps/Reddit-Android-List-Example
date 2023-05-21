package com.codepep.yps.dao

import com.codepep.yps.dto.RedditTopLevelData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditHotApiInter {

    @GET("r/Android/hot.json")
    suspend fun getHotTopics(@Query("after") afterPage: String) : Response<RedditTopLevelData>
}