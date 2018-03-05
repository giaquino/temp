package com.itshere.network

class Response<T>(
    val code: Int,
    val body: T?,
    val errorMessage: String?
) {

  companion object {
    fun <T> create(t: T): Response<T> {
      return Response(200, t, null)
    }
    fun <T> create(throwable: Throwable): Response<T> {
      return Response(500, null, throwable.message)
    }
  }

  fun isSuccessful() = code in 200..299
}