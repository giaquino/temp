package com.itshere.observer

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class LocationObserver(
        val context: Context,
        val lifecycle: Lifecycle,
        val request: LocationRequest,
        val callback: LocationCallback) : LifecycleObserver {

    private val provider = LocationServices.getFusedLocationProviderClient(context)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun start() {
        if (hasLocationPermission()) {
            provider.requestLocationUpdates(request, callback, null)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun stop() {
        if (hasLocationPermission()) {
            provider.removeLocationUpdates(callback)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}