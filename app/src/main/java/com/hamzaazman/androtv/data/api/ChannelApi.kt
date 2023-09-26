package com.hamzaazman.androtv.data.api

import com.hamzaazman.androtv.data.model.ChannelDto
import retrofit2.http.GET

interface ChannelApi {

    @GET("tr.json")
    suspend fun getAllChannel(): List<ChannelDto>
}