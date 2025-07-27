package com.jdf.spacexexplorer.domain.model

/**
 * Result wrapper for handling success and error states in the domain layer
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
    
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading
    
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    fun exceptionOrNull(): Exception? = when (this) {
        is Error -> exception
        else -> null
    }
    
    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> error(exception: Exception): Result<T> = Error(exception)
        fun <T> loading(): Result<T> = Loading
    }
} 