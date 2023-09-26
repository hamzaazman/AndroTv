package com.hamzaazman.androtv.data.repository

import com.hamzaazman.androtv.common.Resource
import com.hamzaazman.androtv.data.api.ChannelApi
import com.hamzaazman.androtv.data.model.ChannelDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChannelRepository @Inject constructor(
    private val channelApi: ChannelApi
) {
    suspend fun getAllChannel(): Flow<Resource<List<ChannelDto>>> = flow {
        emit(Resource.Loading())
        try {
            val response = channelApi.getAllChannel()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        } catch (e: retrofit2.HttpException) {
            emit(Resource.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}