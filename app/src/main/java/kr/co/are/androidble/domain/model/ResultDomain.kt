package kr.co.are.androidble.domain.model

sealed class ResultDomain<out T> {
    data class Success<out T>(val data: T) : ResultDomain<T>()
    data class Error(val exception: Throwable, val isNetwork:Boolean) : ResultDomain<Nothing>()
    data object Loading : ResultDomain<Nothing>()
}