package com.fizarum.get10kusd.app.viewmodels

import androidx.lifecycle.MutableLiveData
import com.fizarum.get10kusd.domain.entities.User

class EditDailyWageViewModel : BaseViewModel() {

    val dailyWageValue = MutableLiveData<String>()

    val userToShow = MutableLiveData<User>()
    val userWithNewDailyWage = MutableLiveData<User>()

    fun cleanUpDailyWageValue() {
        dailyWageValue.value = null
        userWithNewDailyWage.value = null
    }

    fun saveDailyWage() {
        dailyWageValue.value?.toIntOrNull()?.let { newDailyWage ->
            userWithNewDailyWage.value = userToShow.value?.copy(dailyWage = newDailyWage)
        }
    }
}