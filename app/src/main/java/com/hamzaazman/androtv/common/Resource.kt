package com.hamzaazman.androtv.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Error(val exception: Throwable? = null) : Resource<Nothing>
    data class Loading<T>(val data: T? = null) : Resource<T>

    fun <R> mapData(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(
            transform(data)
        )

        is Error -> Error(
            exception
        )

        is Loading -> Loading(
            data?.let { transform(it) }
        )
    }
}

fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return this
        .map<T, Resource<T>> {
            Resource.Success(it)
        }
        .onStart { emit(Resource.Loading()) }
        .catch { emit(Resource.Error(it)) }
}