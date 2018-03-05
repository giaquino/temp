package com.itshere.common.util

import android.databinding.BindingAdapter
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

@BindingAdapter("android:visibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("android:animate_visibility")
fun setVisibilityAnimated(view: View, visible: Boolean) {
    TransitionManager.beginDelayedTransition(view.parent as ViewGroup, AutoTransition())
    view.visibility = if (visible) View.VISIBLE else View.GONE
}