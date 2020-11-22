package com.fizarum.get10kusd.app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fizarum.get10kusd.domain.entities.Goal
import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.usecases.GetEstimatedDaysUseCase
import com.fizarum.get10kusd.domain.usecases.GetUserListUseCase
import com.fizarum.get10kusd.domain.usecases.LoadDailyWagesUseCase
import com.fizarum.get10kusd.domain.usecases.SaveDailyWageUseCase

class UserListViewModel(
    private val getUsersUseCase: GetUserListUseCase,
    private val getEstimatedDaysUseCase: GetEstimatedDaysUseCase,
    private val saveDailyWageUseCase: SaveDailyWageUseCase,
    private val loadDailyWagesUseCase: LoadDailyWagesUseCase
) :
    BaseViewModel(
        getUsersUseCase,
        getEstimatedDaysUseCase,
        saveDailyWageUseCase,
        loadDailyWagesUseCase
    ) {

    private val internalFetchedUserList = MutableLiveData<List<User>>()
    private val internalSavedUserList = MutableLiveData<List<User>>()

    val usersList = MediatorLiveData<MutableList<User>>()
    val showUserList: LiveData<Boolean> = Transformations.map(usersList) { list ->
        mayListBeShown(list)
    }

    init {
        usersList.addSource(internalSavedUserList) { savedList ->
            if (usersList.value.isNullOrEmpty()) {
                usersList.value = savedList.toMutableList()
            } else {
                savedList.forEach { savedUser ->
                    replaceUserWithUpdatedDailyWage(savedUser)
                }
                usersList.value = usersList.value
            }
        }

        usersList.addSource(internalFetchedUserList) { fetchedList ->
            fetchedList?.forEach { fetchedUser ->
                replaceUserWithUpdatedRestInfo(fetchedUser)
            }
            usersList.value = usersList.value
        }
    }

    fun fetchUserList() {
        //load from db
        loadDailyWagesUseCase.execute(
            onSuccess = { savedList ->
                internalSavedUserList.value = savedList
            },
            onError = { t ->
                Log.e("TAG", "cant load saved users: ${t.localizedMessage}")
                internalSavedUserList.value = emptyList()
            },
            onFinished = {},
            params = null
        )
        //fetch from rest api
        getUsersUseCase.execute(
            onSuccess = { list ->
                internalFetchedUserList.value = list.toMutableList()
            },
            onError = { t ->
                Log.e("TAG", "cant fetch users: ${t.localizedMessage}")
                internalFetchedUserList.value = mutableListOf()
            },
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

    fun updateDailyWageForUser(user: User) {
        findUser(user.id)?.let { oldUser ->
            usersList.value = usersList.value?.apply {
                replaceUser(oldUser, user)
            }
        }

        saveDailyWageUseCase.execute(
            onComplete = {
                Log.d("TAG", "user has been saved to db")
            },
            onError = { t ->
                Log.e("TAG", "cant save user to db: ${t.localizedMessage}")
            },
            onFinished = {},
            params = user
        )
    }

    fun mayListBeShown(list: List<User>?): Boolean {
        return list != null && list.isNotEmpty() && list.first().hasAllInformation()
    }

    private fun findUser(id: String) = usersList.value?.find { savedUser -> savedUser.id == id }

    private fun replaceUser(oldUser: User, newUser: User) {
        usersList.value?.remove(oldUser)
        usersList.value?.add(newUser)
    }

    private fun replaceUserWithUpdatedDailyWage(userWithActualDailyWage: User) {
        findUser(userWithActualDailyWage.id)?.let { oldUser ->
            val newUser = oldUser.copy(dailyWage = userWithActualDailyWage.dailyWage)
            replaceUser(oldUser, newUser)
        }
    }

    private fun replaceUserWithUpdatedRestInfo(userWithActualRestInfo: User) {
        findUser(userWithActualRestInfo.id)?.let { oldUser ->
            val newUser = oldUser.copy(
                name = userWithActualRestInfo.name,
                lastName = userWithActualRestInfo.lastName,
                avatarUrl = userWithActualRestInfo.avatarUrl
            )
            replaceUser(oldUser, newUser)
            return
        }
        usersList.value?.add(userWithActualRestInfo)
    }
}