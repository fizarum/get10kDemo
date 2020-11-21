package com.fizarum.get10kusd.app.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> viewModelFactory(crossinline init: () -> VM): () -> ViewModelProvider.Factory =
    {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = init() as T
        }
    }