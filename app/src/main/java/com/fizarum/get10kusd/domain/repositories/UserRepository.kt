package com.fizarum.get10kusd.domain.repositories

import com.fizarum.get10kusd.domain.entities.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun getUsers(): Single<List<User>>

    fun saveUser(user: User): Completable
    fun loadAllUsers(): Single<List<User>>
    fun loadUser(id: String): Single<User>
}