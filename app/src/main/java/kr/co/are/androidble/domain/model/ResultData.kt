package kr.co.are.androidble.domain.model

sealed class ResultData<out T> {
    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val exception: Throwable, val isNetwork:Boolean) : ResultData<Nothing>()
    data object Loading : ResultData<Nothing>()
}