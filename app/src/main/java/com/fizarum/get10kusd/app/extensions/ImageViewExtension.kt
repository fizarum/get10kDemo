package com.fizarum.get10kusd.app.extensions

import android.view.Gravity
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fizarum.get10kusd.R
import com.squareup.picasso.Picasso

@BindingAdapter("avatarUrl")
fun ImageView.avatarUrl(avatarUrl: String?) {
    avatarUrl?.let { url ->
        Picasso.get().load(url)
            .placeholder(R.drawable.ic_baseline_account_circle)
            .resize(width, height).centerCrop(
                Gravity.TOP
            ).into(this)
    } ?: kotlin.run {
        setImageResource(R.drawable.ic_baseline_account_circle)
    }
}