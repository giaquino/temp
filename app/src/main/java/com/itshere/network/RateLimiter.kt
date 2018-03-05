package com.itshere.network

import android.content.SharedPreferences
import android.os.SystemClock

class RateLimiter(private val ttl: Long, private val sharedPreferences: SharedPreferences) {

    fun shouldFetch(key: String): Boolean {
        val now = now()
        val fetchTime = sharedPreferences.getLong(key, 0)
        return now < fetchTime || now - fetchTime >= ttl
    }

    fun recordFetchTime(key: String) {
        sharedPreferences.edit().putLong(key, now()).apply()
    }

    fun now(): Long = SystemClock.uptimeMillis()
}