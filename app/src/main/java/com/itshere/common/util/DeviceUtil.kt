package com.itshere.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings.Secure

object DeviceUtil {

  fun getDeviceName(): String = "${Build.BRAND} ${Build.DEVICE}"

  @SuppressLint("HardwareIds")
  fun getDeviceAndroidId(context: Context): String =
      Secure.getString(context.contentResolver, Secure.ANDROID_ID)
}