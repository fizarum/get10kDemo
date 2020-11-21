package com.fizarum.get10kusd.domain.repositories

import com.fizarum.get10kusd.domain.entities.User
import io.reactivex.Single

interface UserRepository {

    fun getUsers(): Single<List<User>>
}