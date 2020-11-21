package com.fizarum.get10kusd.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fizarum.get10kusd.domain.entities.Goal
import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.usecases.GetEstimatedDaysUseCase
import com.fizarum.get10kusd.domain.usecases.GetUserListUseCase

class UserListViewModel(
    private val getUsersUseCase: GetUserListUseCase,
    private val getEstimatedDaysUseCase: GetEstimatedDaysUseCase
) :
    BaseViewModel(getUsersUseCase, getEstimatedDaysUseCase) {

    private val internalUserList = MutableLiveData<List<User>>(emptyList())

    val userListLiveData: LiveData<List<User>> = Transformations.map(internalUserList) {
        it
    }

    fun fetchUserList() {
        getUsersUseCase.execute(
            onSuccess = { list ->
                internalUserList.value = list
            },
            onError = { internalUserList.value = emptyList() },
            onFinished = {},
            params = null
        )
    }

    // task says that order should be defined by days count to reach the goal
    // but to simplify the logic we can sort by daily wage
    // we can use next statement: bigger daily wage - less days
    // so for sorting days (ASC)
    // lets switch to sorting daily wage (DESC)
    fun sortByDaysASC(users: List<User>): List<User> {
        return users.sortedByDescending { user -> user.dailyWage }
    }

    fun estimatedDaysForUsers(users: List<User>, goal: Goal): List<Pair<User, Int>> {
        return users.map { user ->
            val params = GetEstimatedDaysUseCase.Params(user, goal)
            user to getEstimatedDaysUseCase.execute(params)
        }
    }
}