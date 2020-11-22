package com.fizarum.get10kusd.app.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun View.setIsVisible(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}