package com.itshere.common.util

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.design.internal.SnackbarContentLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.SnackbarLayout
import android.view.View
import com.itshere.R

object SnackbarUtil {

  /**
   * Shows a Snackbar that redirects to application settings
   */
  fun showSnackbarToSettings(view: View, messageResource: Int) {
    val intent = Intent().apply {
      action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
      data = Uri.fromParts("package", view.context.packageName, null)
    }
    Snackbar.make(view, messageResource, Snackbar.LENGTH_LONG)
        .setAction(R.string.permission_rationale_settings, { view.startActivity(intent) })
        .show()
  }

  @SuppressLint("RestrictedApi")
  fun Snackbar.setActionLetterSpacing(spacing: Float): Snackbar {
    try {
      val layout = (this.view as SnackbarLayout).getChildAt(0) as SnackbarContentLayout
      layout.actionView.letterSpacing = spacing
    } catch (ignore: Exception) {
    }
    return this
  }
}