package com.fizarum.get10kusd.app.viewmodels

import androidx.lifecycle.ViewModel
import com.fizarum.get10kusd.domain.usecases.UseCase

open class BaseViewModel(vararg useCases: UseCase) : ViewModel() {

    private var useCaseList: MutableList<UseCase> = mutableListOf()

    init {
        useCaseList.addAll(useCases)
    }

    override fun onCleared() {
        super.onCleared()
        useCaseList.forEach {
            it.dispose()
        }
    }
}