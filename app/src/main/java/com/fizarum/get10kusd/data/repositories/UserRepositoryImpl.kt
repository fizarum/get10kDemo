package com.fizarum.get10kusd.data.repositories

import com.fizarum.get10kusd.data.adapters.UserAdapter
import com.fizarum.get10kusd.data.rest.RestClient
import com.fizarum.get10kusd.data.rest.UsersService
import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.repositories.UserRepository
import io.reactivex.Single

class UserRepositoryImpl(restClient: RestClient): UserRepository {

    private val userService = restClient.retrofit.create(UsersService::class.java)

    override fun getUsers(): Single<List<User>> {
        return userService.getUserList().map { response ->
            response.list.map { userDTO ->
                UserAdapter.fromDTO(userDTO)
            }
        }
    }
}